package com.itheima.mq;

import com.itheima.constant.MessageConstant;
import com.itheima.interfaces.UserTestService;
import com.itheima.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * Created by 我自己 on 2020-03-13.
 * 消费同步redis数据
 */
@Component
public class UserLinsters {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    UserTestService userTestService;

    @JmsListener(destination = "UserCount",containerFactory =
            "jmsQueueListener")
    public void consumePaymentResultMapMessage(MapMessage mapMessage) throws JMSException {
        Jedis jedis=redisUtil.getJedis();
        try {
            String id=mapMessage.getString("uid");
            if (StringUtils.isNotBlank(id)){
                //删除 查询  如果没有的也是有过期时间的
                jedis.del(MessageConstant.UserByid+id);
                //查询
                userTestService.muserById(id);
            }
            jedis.del(MessageConstant.UserAll);
            //查询所有的接口
            userTestService.muserAll();
        }catch (Exception e){
                e.printStackTrace();
        }finally {
            if (jedis!=null){
                jedis.close();

            }
        }

    }
    }
