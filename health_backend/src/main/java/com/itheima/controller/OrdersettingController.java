package com.itheima.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.registry.multicast.MulticastRegistry;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.interfaces.OrdersettingService;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.util.POIUtils;

@RestController
public class OrdersettingController {

	
	@Reference(interfaceClass=OrdersettingService.class)
	private OrdersettingService ordersettingService;
	
	//上传模板文件 录入到数据库
	//每次读取 添加就行了
	@RequestMapping("uploadXLS")
	public Result uploadXLS(@RequestParam("excelFile") MultipartFile excelFile) throws IOException {
		Result result=new Result();
		try {

			List<String[]> val=POIUtils.readExcel(excelFile);
			if (val==null||val.size()<=0) {
				result.setFlag(false);
				result.setMessage("改模板没有数据导入!");
				return result;
			}
			
			List<OrderSetting> orderSettingList=new ArrayList<OrderSetting>();
			for (String[] han : val) {
				OrderSetting orderSetting=new OrderSetting();
				orderSetting.setOrderDate(new Date(han[0]));
				orderSetting.setNumber(Integer.parseInt(han[1]+""));
				orderSettingList.add(orderSetting);
			}
			
			//集合传过去添加数据库
			if (orderSettingList!=null&&orderSettingList.size()>0) {
				ordersettingService.OrderSettingInserts(orderSettingList);
			}
			result.setFlag(true);
			result.setMessage(MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
			
		} catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.IMPORT_ORDERSETTING_FAIL);
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping("getYearOrderSettin")
	public Result getYearOrderSettin(String day) throws ParseException {
		Result result=new Result();
		
		List<OrderSetting> list=ordersettingService.getYearOrderSettin(day);
		List<Map> listMaps=new ArrayList<Map>();
		try {
			System.out.println(list+"//"+list.size());
			for (OrderSetting orderSetting : list) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("date", orderSetting.getOrderDate().getDate());
				
				map.put("number", orderSetting.getNumber());
				map.put("reservations", orderSetting.getReservations());
				listMaps.add(map);
			}
		
			result.setData(	JSON.toJSONString(listMaps));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return result;
		
	}
	
	
	@RequestMapping("editNumberByDate")
	public Result editNumberByDate(@RequestBody Map map) {
		Result result=new Result();
		try {
			ordersettingService.editNumberByDate(map);
			result.setFlag(true);
			result.setMessage(MessageConstant.ORDER_RENSHU_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			result.setFlag(false);
			result.setMessage(e.getMessage());
		}
		
		System.out.println(map.get("number")+"//"+map.get("orderDate"));
		return result;
	}
	
}
