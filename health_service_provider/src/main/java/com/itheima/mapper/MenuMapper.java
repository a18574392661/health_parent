package com.itheima.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itheima.pojo.Menu;

public interface MenuMapper {

	
	List<Menu> queryUserMenu(@Param("username")String username);

}
