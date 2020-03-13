package com.itheima.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.interfaces.OrdersettingService;
import com.itheima.mapper.OrdersettingMapper;
import com.itheima.pojo.OrderSetting;

@Service(interfaceClass=OrdersettingService.class)
@Transactional
public class OrdersettingServicelmpl implements OrdersettingService {
	
	@Autowired
	 OrdersettingMapper ordersettingMapper;
	
	@Override
	public void OrderSettingInserts(List<OrderSetting> list) {
			
		for (OrderSetting orderSetting : list) {
				
				//查询有没有一样的日期 
				OrderSetting o=ordersettingMapper.selectOneDate(orderSetting);
				if (o!=null) {
					//修改数量 移除集合当前对象
					o.setNumber(orderSetting.getNumber());
					ordersettingMapper.updateNumberDate(o);
					list.remove(orderSetting);
				}
		}
		
		ordersettingMapper.inserts(list);
	}

	@Override
	public List<OrderSetting> getYearOrderSettin(String day) {
			//拿到年跟月
		String year=day.split("-")[0];
		String moth=day.split("-")[1];
		System.out.println(year+"??"+moth);
		List<OrderSetting> list=ordersettingMapper.getYearOrderSettin( year, moth);
		return list;
	}

	@Override
	public void editNumberByDate(Map map) {
		//留个bug 应该要修改的预约人数必须大于等于已经预约人数
	int c=ordersettingMapper.editNumberByDate(map);
		
		
	}

}
