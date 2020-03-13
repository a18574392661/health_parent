package com.itheima.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.interfaces.AddressService;
import com.itheima.interfaces.OrderService;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.util.RedisUtil;

import redis.clients.jedis.Jedis;

@Controller
public class OrderController {

	
	@Autowired
	private RedisUtil redisUtil;
	
	
	@Reference
	private OrderService orderService;
	
	@RequestMapping("ordersubmit")
	@ResponseBody
	public Result ordersubmit(@RequestBody(required=false) Map<String,Object> map) {
		
		Result result=new Result();
		String code=map.get("validateCode")+"";
		String telephone=map.get("telephone")+"";
		if (StringUtils.isBlank(code)||StringUtils.isBlank(telephone)) {
			result.setFlag(false);
			result.setMessage(MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
			return result;
		}
		//判断是否过期 
		String mobileCodeKey=MessageConstant.GET_CODE_KEY+":"+telephone;
		Jedis jedis=redisUtil.getJedis();
		String codeCathc=jedis.get(mobileCodeKey);
		try {
			System.out.println(codeCathc+"//"+code+"//等于否"+code.equals(codeCathc));
			if (StringUtils.isNotBlank(code)&&StringUtils.isNotBlank(codeCathc)&&code.equals(codeCathc)) {
				
				//判断是否过期
				if (jedis.ttl(mobileCodeKey)==-2) {
					result.setFlag(false);
					result.setMessage("当前验证码已经过期");
				}else {
					//已经过期了
				int orderId=orderService.insertNumberOrder(map);
					//验证码正确执行业务逻辑
				if (orderId==0) {
					result.setFlag(false);
					result.setMessage("预约失败");
				}else {
					result.setFlag(true);
					result.setMessage(MessageConstant.ORDER_SUCCESS);	
				}
				
					
				}
				
			}else {
				result.setFlag(false);
				result.setMessage("验证码输入错误!");
			}
		} catch (Exception e) {
			result.setFlag(false);
			result.setMessage(e.getMessage());
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
	@RequestMapping("orderfindById")
	public Result orderfindById(String id) {
		Result result=new Result();
		try {
			
			result.setData(orderService.orderfindById(id));
			result.setFlag(true);
			
		} catch (Exception e) {
			result.setFlag(false);
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	@Reference(interfaceClass=AddressService.class)
	private AddressService addressService;
	
	@RequestMapping("addressAll")
	@ResponseBody
	public Result addressAll() {
		Result result=new Result();
		try {
			result.setFlag(true);
			result.setData(addressService.queryAddresAll());
		} catch (Exception e) {
			result.setFlag(false);
			e.printStackTrace();
		}
		
		return result;
	}
}
