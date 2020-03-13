package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.interfaces.UserTestService;
import com.itheima.pojo.Muser;
import com.itheima.util.ActiveMQUtil;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.*;
import java.util.Map;

/**
 * Created by 我自己 on 2020-03-13.
 */

@Controller
public class ListController {


    @Reference(interfaceClass = UserTestService.class)
    private UserTestService userTestService;

    @RequestMapping("to_list")
    public  String to_list(ModelMap map){

        //查询所有页面
        map.put("list",userTestService.muserAll());

        return  "pages/Tests/list";
    }


    @RequestMapping("to_add")
    public  String to_add(ModelMap map){

        //查询所有页面
        map.put("roles",userTestService.mroleAll());

        return  "pages/Tests/add";
    }

    @RequestMapping("to_edit")
    public  String to_edit(ModelMap map,String id){
        map.put("roles",userTestService.mroleAll());
        //查询所有页面
        map.put("u",userTestService.muserById(id));

        return  "pages/Tests/edit";
    }

    @RequestMapping("edit")
    public  String edit(ModelMap map,Muser muser){
     userTestService.muserEdit(muser);

        Map<String,Object> map1=new HashedMap();
        map1.put("uid",muser.getId());
        sendTimeMessag(
                "UserCount",map1,0);//清空id 当前id的缓存

        return "redirect:/to_list";
    }


    @RequestMapping("add")
    public  String add(ModelMap map,Muser muser){
        userTestService.muserAdd(muser);
        //开启队列 队列去消费


        sendTimeMessag("UserCount",null,0);//清空id 当前id的缓存


        return "redirect:/to_list";
    }

    @RequestMapping("del")
    public  String del(ModelMap map,String id){
        userTestService.delMuser(id);

        Map<String,Object> map1=new HashedMap();
        map1.put("uid",id);

        sendTimeMessag(
                "UserCount",map1,0);//清空id 当前id的缓存

        return "redirect:/to_list";
    }


@Autowired
    private ActiveMQUtil activeMQUtil;


    //增删改同步redis缓存
    public void sendTimeMessag(String sendName, Map<String,Object> map,int time) {
        System.out.println(activeMQUtil+"cnm");
        Connection connection = null;
        Session session = null;

        try {
            connection=activeMQUtil.getConnectionFactory().createConnection();
            session=connection.createSession(true, Session.SESSION_TRANSACTED);
            //创建一个
            Queue payhment_success_queue = session.createQueue(sendName);
            MessageProducer producer = session.createProducer(payhment_success_queue);

            MapMessage mapMessage = new ActiveMQMapMessage();// hash结构
            if (map!=null) {
                for (String mkey : map.keySet()) {
                    System.out.println(mkey+"//"+map.get(mkey));
                    mapMessage.setString(mkey,map.get(mkey)+"");//次数 订单号
                }
            }
            //大于0的话 设置延时队列
            if (time>0) {
                mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time);
            }

            producer.send(mapMessage);

            session.commit();

        } catch (Exception e) {
            try {
                session.rollback();
            } catch (JMSException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try {
                if (connection!=null)
                connection.close();
            } catch (JMSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

}
