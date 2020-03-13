

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.itheima.QuartzDemoApplication;
import com.itheima.constant.MessageConstant;
import com.itheima.service.MealImageTimeService;
import com.itheima.util.QiniuUtils;
import com.itheima.util.RedisUtil;

import redis.clients.jedis.Jedis;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuartzDemoApplication.class) // 这里的Application是springboot的启动类名
@WebAppConfiguration

public class TestRedis {
	
	@Autowired
	RedisUtil redisUtil;
	
	
	@Autowired
	private MealImageTimeService mealImageTimeService;
	/*
	 * 定时器删除
	 */
	@Test
	public void s() {
	 Jedis jedis=redisUtil.getJedis();
	jedis.sadd(MessageConstant.Set_SETMEAL_Image_UpDB, "http://q67s0aau1.bkt.clouddn.com/4e5dec7a-7e58-480d-b96b-30bb5c0a0e6c1.jpg");
	 //获得上传七牛云的所有图片 
	System.out.println(mealImageTimeService+"//"+jedis);
	 Set<String> notDbImage=jedis.smembers(MessageConstant.Set_SETMEAL_Image_UpNODB);
	 Set<String> DbImage=jedis.smembers(MessageConstant.Set_SETMEAL_Image_UpDB);
	 if (notDbImage!=null&&notDbImage.size()>0&&DbImage!=null&&DbImage.size()>0) {

		 
	}
	
	}

	
	
	//两个集合的二差集
	public  Set<String> setCha(Set nodbImage,Set dbImage) {
		
		
		  Set<String> result = new HashSet<String>();  
	        result.clear();
	        result.addAll(nodbImage);
	        result.removeAll(dbImage);
	        return result ;
	}
}
