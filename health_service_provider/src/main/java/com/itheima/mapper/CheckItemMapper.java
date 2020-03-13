package com.itheima.mapper;

import java.util.List;

import com.itheima.pojo.CheckItem;

public interface CheckItemMapper {

	
	public int checkAdd(CheckItem checkItem);

	public List<CheckItem> checkAll(CheckItem checkItem);

	public Integer selectItemGroupby(Integer id);

	public int checkDel(Integer id);

	public CheckItem checkByid(Integer id);
	
	public int updateCheck(CheckItem checkItem);
	
	
}
