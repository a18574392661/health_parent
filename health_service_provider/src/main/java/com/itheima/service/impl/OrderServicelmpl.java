package com.itheima.service.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.interfaces.OrderService;
import com.itheima.mapper.OrderMapper;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;

@Service(interfaceClass=OrderService.class)
@Transactional
public class OrderServicelmpl implements OrderService {

	@Autowired
	OrderMapper orderMapper;
	
	@Override
	public int insertNumberOrder(Map<String, Object> map) {
		
		if (map==null||StringUtils.isBlank(map.get("idCard")+"")) {
			
			throw new RuntimeException("预约失败!");
		}
		//先查询 用户表存不存在 存在
		Member member=orderMapper.numberCartAll(map.get("idCard")+"",map.get("telephone")+"");
		Integer numberId=null;
		if (member!=null) {
			//以防万一 相同时间不能预约 
		
			//存在则根据身份证信息修改 
			//然后查询大于当前时间的是否预约过 已经预约了不能预约 没预约添加
			int count=orderMapper.orderCountState(member.getId());//查询是否预约过
			if (count>0) {
				throw new RuntimeException(MessageConstant.HAS_ORDERED);
			}
			numberId=member.getId();
			orderMapper.numberUpdate(map);
			
			//添加预约信息
		}
		else {
			Member m=new Member();
			m.setName(map.get("name")+"");
			m.setPhoneNumber(map.get("telephone")+"");
			m.setIdCard(map.get("idCard")+"");
			m.setSex(map.get("sex")+"");
			//直接添加 用户 预约信息
			int c=orderMapper.numberSave(m);
			
			if (c<=0) {
				throw new RuntimeException("预约失败");
			}
			numberId=m.getId();
		}
		
		if (numberId==null) {
			throw new RuntimeException("预约失败!");
		}
		Order order=new Order();
		order.setMemberId(numberId);
		if (map.get("type")==null) {
			order.setOrderType(1+"");
		}else {
			order.setOrderType(2+"");
		}
		
		order.setOrderStatus(0+""); //未到诊
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			order.setOrderDate(simpleDateFormat.parse(map.get("orderDate")+""));
		} catch (ParseException e) {
			
			e.printStackTrace();
			return 0;
		}
			order.setSetmealId(Integer.parseInt(map.get("setmealId")+""));
			order.setAddressId(Integer.parseInt(map.get("addressId")+""));
	
			int c2=orderMapper.orderSave(order);
			if (c2<=0) {
				throw new RuntimeException("预约失败");
			}
			return  order.getId();
		
		

	}

	@Override
	public Map orderfindById(String id) {
		// TODO Auto-generated method stub
		return orderMapper.queryOrderBynumber(id);
	}

	@Override
	public List<Integer> queryNumberMthCount(List<String> months) {
		List<Integer> count=new ArrayList<Integer>();
		if (months==null||months.size()<=0) {
			throw new RuntimeException("月份统计异常");
		}
		
		for (String m : months) {
			count.add(orderMapper.queryNumberCount(m+".1",m+".31"));
		}
		
		
		return count;
	}

	@Override
	public List<Map> orderNumCount() {
		// TODO Auto-generated method stub
		return orderMapper.orderNumCount();
	}

	@Override
	public Map<String, Object> getBusinessReportData() {
		List<Map<String, Object>> msList=new ArrayList<Map<String,Object>>();
		Map<String, Object> parsMap=new HashMap<String, Object>();//封装参数的
		Map<String, Object> msMap=new HashMap<String, Object>();
		SimpleDateFormat simpleDateFormat=null;
		//当天
		simpleDateFormat=new SimpleDateFormat("yyyy-MM-d");
		Calendar calendar=Calendar.getInstance();
		String 今天的=simpleDateFormat.format(calendar.getTime());
		//查询今天新增的
		parsMap.put("todayNewMember", 今天的);
		Integer todayNewMember=orderMapper.queryPoiOrders(parsMap);
		//今天的预约数
		Integer todayOrderNumber=orderMapper.queryOrderCount(parsMap); 
		//今天预约到诊的
		parsMap.put("sta", 1);
		Integer todayVisitsNumber=orderMapper.queryOrderCount(parsMap); 
		parsMap.clear();
		//查询总的
		Integer totalMember=orderMapper.queryPoiOrders(parsMap);
		Integer orderCount=orderMapper.queryOrderCount(parsMap); //总人数
		//当前月
		parsMap.put("thisMonthNewMember", "当前月");
		Integer thisMonthNewMember=orderMapper.queryPoiOrders(parsMap); //当前月会有数
		Integer thisMonthOrderNumber=orderMapper.queryOrderCount(parsMap); //当前月预约数
		//到诊
		parsMap.put("sta", 1);
		Integer thisMonthVisitsNumber=orderMapper.queryOrderCount(parsMap); 
		parsMap.clear();
		//当前周
		parsMap.put("thisWeekNewMember", "当前周");
		Integer thisWeekNewMember=orderMapper.queryPoiOrders(parsMap); //本周会员数
		Integer thisWeekOrderNumber=orderMapper.queryOrderCount(parsMap); //本周与约束
		parsMap.put("sta", 1);
		Integer thisWeekVisitsNumber=orderMapper.queryOrderCount(parsMap);//本周到诊数
		parsMap.clear();
		
		//查询每个套餐的占用率
		List<Map> list=orderMapper.orderNumCount();
		msMap.put("reportDate", 今天的);
		msMap.put("todayNewMember", todayNewMember);
		msMap.put("totalMember", totalMember);
		msMap.put("thisWeekNewMember", thisWeekNewMember);
		msMap.put("thisMonthNewMember", thisMonthNewMember);
		msMap.put("todayOrderNumber", todayOrderNumber);
		msMap.put("todayVisitsNumber", todayVisitsNumber);
		msMap.put("thisWeekOrderNumber", thisWeekOrderNumber);
		msMap.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
		msMap.put("thisMonthOrderNumber", thisMonthOrderNumber);
		msMap.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
		
		for (Map map : list) {
			Map<String, Object> ms=new HashMap<String, Object>();
			ms.put("name", map.get("name"));
			ms.put("setmeal_count", map.get("count"));
			//(float) 3*100/10
			ms.put("proportion",Float.parseFloat(map.get("count")+"")/orderCount*100+"%");
		 msList.add(ms);
		}
		
		//查询预约时间统计数据
		msMap.put("hotSetmeal", msList);
		return msMap;
	}

	@Override
	public PageResult orderList(Order order, Integer currentPage, Integer pageSize) {
		PageResult pageResult=new PageResult();
		
		try {
			PageHelper.startPage(currentPage, pageSize);
			//查询
		List<Order> list=orderMapper.orderList(order);
		PageInfo<Order> pageInfo=new PageInfo<Order>(list);
		pageResult.setTotal(pageInfo.getTotal());
		pageResult.setRows(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageResult;
	}

	@Override
	public void handleUpdateStatus(String id) {
		
		orderMapper.handleUpdateStatus(id);
		
	}

	@Override
	public void handleDelete(String id) {
		orderMapper.handleDelete(id);
		
	}

}
