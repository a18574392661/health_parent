package com.itheima.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.interfaces.AddressService;
import com.itheima.pojo.Address;

@RestController
public class AddressController {
	
	
	@Reference(interfaceClass=AddressService.class)
	private AddressService addressService;
	
	@RequestMapping("AddressAll")
	
	public Result AddressAll() {
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
	
	@RequestMapping("AddressDel")
	public Result AddressDel(String id) {
		Result result=new Result();
		addressService.AddressDel(id);
		
		result.setFlag(true);
		
		
		return result;
		
		
	}
	
	@RequestMapping("AddressAdd")
	public Result AddressAdd(@RequestBody Address address) {
		Result result=new Result();
		try {
			addressService.AddressAdd(address);
			result.setFlag(true);
			result.setMessage("新增地址成功");
		} catch (Exception e) {
			result.setFlag(false);
			result.setMessage("新增地址失败");
			e.printStackTrace();
		}
		
		
		
		
		
		return result;
		
		
	}
	

}
