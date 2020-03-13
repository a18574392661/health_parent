package com.itheima.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.interfaces.AddressService;
import com.itheima.mapper.AddressMapper;
import com.itheima.pojo.Address;

@Transactional
@Service(interfaceClass=AddressService.class)
public class AddressServicelmpl implements AddressService {

	
	@Autowired
	private AddressMapper addressMapper;
	
	@Override
	public List<Address> queryAddresAll() {
		// TODO Auto-generated method stub
		return addressMapper.queryAddresAll();
	}

	
	
	@Override
	public void AddressDel(String id) {
		
		 addressMapper.AddressDel(id);
	}



	@Override
	public void AddressAdd(Address address) {
		// TODO Auto-generated method stub
		addressMapper.AddressAdd(address);
	}

}
