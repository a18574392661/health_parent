package com.itheima.interfaces;

import java.util.List;

import com.itheima.pojo.Address;

public interface AddressService {
	
	
	public List<Address> queryAddresAll();

	public void AddressDel(String id);

	public void AddressAdd(Address address);
	
}
