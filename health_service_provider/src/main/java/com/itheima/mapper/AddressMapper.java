package com.itheima.mapper;

import java.util.List;

import com.itheima.pojo.Address;

public interface AddressMapper {
	public List<Address> queryAddresAll();

	public int AddressDel(String id);

	public void AddressAdd(Address address);
}
