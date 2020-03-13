package com.itheima.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.interfaces.MealService;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;
import com.itheima.util.CreateStaticPage;
import com.itheima.util.RedisUtil;
import com.itheima.util.SMSUtils;
import com.itheima.util.ValidateCodeUtils;

import redis.clients.jedis.Jedis;

@Controller
public class MealMobileController {

	
	@Reference(interfaceClass=MealService.class)
	private MealService mealService;
	
	//用于页面跳转手机页面
		@RequestMapping("{path}.html")
		public String index(@PathVariable(required=false) String path) {
			
		
			return "pages/"+path;
		}
		
		
		//查询所有套餐
		@RequestMapping("getAllSetmeal")
		@ResponseBody
		public Result getAllSetmeal() {
			Result result=mealService.getAllSetmeal();
			
			return result;
			
		}
		
		
		//根据id查询 缓存
				@RequestMapping("findById")
				@ResponseBody
				public Result findById(String id) {
					Result result=mealService.findById(id);
					
					return result;
					
				}
				
				
				@Autowired
				private RedisUtil redisUtil;
			
				//发送手机验证码
				@RequestMapping("sendCode")
				@ResponseBody
				public Result sendCode(String telephone) {
					Result result=new Result();
					
					String mobileCodeKey=MessageConstant.GET_CODE_KEY+":"+telephone;
					Jedis jedis=redisUtil.getJedis();
					String codeCathc=jedis.get(mobileCodeKey);
					try {
						if (StringUtils.isNotBlank(codeCathc)&&jedis.ttl(codeCathc)!=-2) {
							//每次先判断这个手机号码是否发过验证码
							result.setMessage("当前手机已经发son过验证码没过期请不要重复发送!");
							result.setFlag(false);
							return result;
						}
						//随机生成数字
						String code=ValidateCodeUtils.generateValidateCode4String(4);
						//发送验证码保存到redis
						SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
						//返回回调时间 前段倒计时
						jedis.setex(mobileCodeKey, 60, code);
						System.out.println(jedis.ttl(mobileCodeKey));
						result.setData(jedis.ttl(mobileCodeKey));
						result.setFlag(true);
					} catch (Exception e) {
						result.setFlag(false);
						result.setMessage("手机验证码发送失败");
						e.printStackTrace();
					}
					finally {
						if (jedis!=null) {
							jedis.close();
						}
					}
					
					
					return result;
				}
				
				
				@ResponseBody
				@RequestMapping("logins")
				public Result login(String telephone,String validateCode) {
					Member member=new Member();
					member.setValidateCode(validateCode);
					member.setPhoneNumber(telephone);
					System.out.println(telephone+"//"+validateCode);
					Result result=new Result();
					Jedis jedis=redisUtil.getJedis();
					String umsLoginKey=MessageConstant.GET_CODE_KEY+":"+member.getPhoneNumber();//验证用户登录 按理来说要传2个key
					String code=jedis.get(umsLoginKey);
					System.out.println(umsLoginKey+"//"+code);
					try {
						System.out.println(code+"ss"+member.getValidateCode()+".."+code.equals(member.getValidateCode()));
						if (StringUtils.isNotBlank(code)&&StringUtils.isNotBlank(member.getValidateCode())&&code.equals(member.getValidateCode())) {
							//业务逻辑
							mealService.login(member);
							result.setMessage(MessageConstant.UMS_LOGIN_FAIT);
							result.setFlag(true);	
							
						}else {

							result.setMessage("验证码错误!");
							result.setFlag(false);	
						}
						
						
					} 
					catch (RuntimeException e) {
						result.setFlag(false);
						result.setMessage(MessageConstant.UMS_LOGIN_FAIT);
						 e.printStackTrace();
					}
					catch (Exception e) {
						result.setFlag(false);
						result.setMessage(MessageConstant.UMS_LOGIN_FAIT);
						e.printStackTrace();
					}
					finally {
						if (jedis!=null) {
							jedis.close();
						}
					}
					return result;
					
				}
}
