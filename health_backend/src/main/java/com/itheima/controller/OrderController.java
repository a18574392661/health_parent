package com.itheima.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.interfaces.OrderService;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;

@RestController
public class OrderController {

	
	@Reference(interfaceClass=OrderService.class)
	private OrderService orderService;
	
	//查询预约列表 
	@RequestMapping("orderList")
	public PageResult orderList(@RequestBody(required=false) QueryPageBean queryPageBean) {
		Order order=new Order();
		order.setCode(queryPageBean.getQueryString());
		PageResult pageResult=orderService.orderList(order,queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
		return pageResult;
	}
	
	//修改状态已经到诊
	@RequestMapping("handleUpdateStatus")
	public Result handleUpdateStatus(String id) {
		Result result=new Result();
		try {
			orderService.handleUpdateStatus(id);
			result.setFlag(true);
			result.setMessage(MessageConstant.ORDER_UPDATE_STATUS_SUCESS);
		} catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.ORDER_UPDATE_STATUS_ERROR);
			 e.printStackTrace();
		}
		
		return result;
	}
	
	@RequestMapping("handleDelete")
	public Result handleDelete(String id) {
		Result result=new Result();
		try {
			orderService.handleDelete(id);
			result.setFlag(true);
			result.setMessage(MessageConstant.ORDER_UPDATE_DELETE_SUCESS);
		} catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.ORDER_UPDATE_DELETE_ERROR);
			 e.printStackTrace();
		}
		
		return result;
		
		
	}
	
	//手机电话预约 后台人员 
	@RequestMapping("orderTel")
	public Result orderTel(@RequestBody Map<String, Object> map) {
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		//拿到时间 
	
		Result result=new Result();
		
		try {
			String dString=map.get("orderDate")+"";
			Date orderDate=simpleDateFormat.parse(dString);
			if (orderDate.compareTo(new Date())==-1) {
				result.setFlag(false);
				result.setMessage("预约时间不能小于系统时间!");
				return result;
			}
			
			orderService.insertNumberOrder(map);
			result.setFlag(true);
			result.setMessage(MessageConstant.ORDER_SUCCESS);
		} 
		catch (RuntimeException e) {
			result.setFlag(false);
			result.setMessage(e.getMessage());
				e.printStackTrace();
		}
		catch (Exception e) {
			result.setFlag(false);
			result.setMessage("预约失败");
			e.printStackTrace();
		}
		
		
		
		
		
		return result;
	}
	
	public static void main(String[] args) throws ParseException {
		String string="2022-5-21";
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date date=simpleDateFormat.parse(string);
		System.out.println(date.compareTo(new Date()));
	}
}
