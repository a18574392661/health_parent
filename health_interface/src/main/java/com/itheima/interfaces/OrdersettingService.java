package com.itheima.interfaces;

import java.util.List;
import java.util.Map;

import com.itheima.pojo.OrderSetting;

public interface OrdersettingService {

	
	//批量添加(导入)预约列表
	public void OrderSettingInserts(List<OrderSetting> list);

	public List<OrderSetting> getYearOrderSettin(String day);

	public void editNumberByDate(Map map);
}
