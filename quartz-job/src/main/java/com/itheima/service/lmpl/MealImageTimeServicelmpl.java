package com.itheima.service.lmpl;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.constant.MessageConstant;
import com.itheima.service.MealImageTimeService;
import com.itheima.util.ListUtils;
import com.itheima.util.QiniuUtils;
import com.itheima.util.RedisUtil;

import redis.clients.jedis.Jedis;


@Service
public class MealImageTimeServicelmpl implements MealImageTimeService {
	
	
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public void deleteHisImage() {
		
		Jedis jedis=redisUtil.getJedis();
		System.out.println(jedis);
		try {
			 //拿到非数据库图片 数据库图片 
			 Set<String> notDbImage=jedis.smembers(MessageConstant.Set_SETMEAL_Image_UpNODB);
			 Set<String> DbImage=jedis.smembers(MessageConstant.Set_SETMEAL_Image_UpDB);
			 if (notDbImage!=null&&notDbImage.size()>0&&DbImage!=null&&DbImage.size()>0) {
				
				 
				 Set<String> chaImage=ListUtils.setCha(notDbImage, DbImage);
				 for (String url : chaImage) {
					 System.out.println(url);
					 String oo=url.substring(url.lastIndexOf("/",url.lastIndexOf("/"))+1);
					 //删除图片之后缓存 删除这个 以免总是去定时删除没有的图
					// QiniuUtils.deleteFileFromQiniu(oo);
					 //jedis.srem(MessageConstant.Set_SETMEAL_Image_UpNODB, url);
				}
				 
			}
			
			
			
		} catch (Exception e) {
		 e.printStackTrace();
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}

	}

}
