package com.itheima.interfaces;

import java.util.List;
import java.util.Map;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Order;

public interface OrderService {
	
	//用户预约业务
	public int insertNumberOrder(Map<String,Object> map);

	public Map orderfindById(String id);

	public List<Integer> queryNumberMthCount(List<String> months);
	
	public List<Map> orderNumCount();

	public Map<String, Object> getBusinessReportData();

	public PageResult orderList(Order order, Integer currentPage, Integer pageSize);

	public void handleUpdateStatus(String id);

	public void handleDelete(String id);
}
