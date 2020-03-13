package com.itheima.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.itheima.pojo.Member;
import com.itheima.pojo.Order;

public interface OrderMapper {

	
	public Member numberCartAll(@Param("idCard")String card,@Param("telephone")String telephone);
	public int numberSave(Member member);
	public int numberUpdate(Map<String,Object> map);
	public int orderSave(Order order);
	
	public int orderCountState(int numberId);
	
	//用户同一个时间预约两次
	public int queryOrderDate(Map<String, Object> map);
	
	//预约信息是哪个人预约的
	public Map queryOrderBynumber(@Param("id")String id);
	
	//批量查询每个月的人数
	public int queryNumberCount(@Param("month1")String m,@Param("month2") String string);
	
	
	public List<Map> orderNumCount();
	
	
	public Integer queryPoiOrders(Map<String, Object> parsMap);
	//预约
	public Integer queryOrderCount(Map<String, Object> parsMap);
	
	public List<Order> orderList(Order order);
	public void handleUpdateStatus(@Param("id")String id);
	
	public void handleDelete(@Param("id")String id);
}
