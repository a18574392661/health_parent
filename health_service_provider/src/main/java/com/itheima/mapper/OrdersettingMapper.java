package com.itheima.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.itheima.pojo.OrderSetting;

import tk.mybatis.mapper.common.Mapper;

public interface OrdersettingMapper{

	
	public int inserts(@Param("list")List<OrderSetting> list);
	
	//先查询再修改 
	public  OrderSetting selectOneDate(OrderSetting orderSetting);
	public  int 	updateNumberDate(OrderSetting orderSetting);

	public List<OrderSetting> getYearOrderSettin(@Param("year")String year,@Param("moth") String moth);

	public int editNumberByDate(Map map);
	
}
