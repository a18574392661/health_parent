package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.interfaces.UserTestService;
import com.itheima.mapper.UserTestMapper;
import com.itheima.pojo.Mrole;
import com.itheima.pojo.Muser;
import com.itheima.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by 我自己 on 2020-03-13.
 */
@Service(interfaceClass = UserTestService.class)
@Transactional
public class UserTestServicelmpl implements UserTestService {



    @Autowired
    private UserTestMapper userTestMapper;


    @Autowired
    private RedisUtil redisUtil;




    @Override
    public List<Muser> muserAll() {
        List<Muser> list=null;
        Jedis jedis=redisUtil.getJedis();
        try {
                //先查询缓存
            jedis.select(1);//切换库
            String val=   jedis.get(MessageConstant.UserAll);
            if (StringUtils.isNotBlank(val))
                list= JSON.parseArray(val,Muser.class);//转换集合
            else{
                //数据库查询 保存集合 过期时间
               list=userTestMapper.muserAll();
                jedis.set(MessageConstant.UserAll,JSON.toJSONString(list));

            }

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if (jedis!=null){
                jedis.close();

            }
        }

        return list;
    }

    @Override
    public Muser muserById(String id) {
        Jedis jedis=redisUtil.getJedis();
        jedis.select(1);
      String val=jedis.get(MessageConstant.UserByid+id);

        Muser muser=null;
        try {
            if (StringUtils.isNotBlank(val))
                muser=JSON.parseObject(val,Muser.class);
            else {
                muser=userTestMapper.muserById(id);
                if (muser!=null){
                    jedis.set(MessageConstant.UserByid+id,JSON.toJSONString(muser));

                }else{
                    //缓存穿透
                    jedis.setex(MessageConstant.UserByid+id,60,JSON.toJSONString(muser));

                }

            }

        }catch (Exception execution){
            execution.printStackTrace();

        }finally {
                if (jedis!=null){
                    jedis.close();

                }
        }
        return muser;
    }

    @Override
    public void delMuser(String id) {

        userTestMapper.delMuser(id);
    }

    @Override
    public void muserAdd(Muser muser) {
        userTestMapper.muserAdd(muser);
    }

    @Override
    public void muserEdit(Muser muser) {
        userTestMapper.muserEdit(muser);
    }

    @Override
    public List<Mrole> mroleAll() {


        return userTestMapper.mroleAll();
    }
}
