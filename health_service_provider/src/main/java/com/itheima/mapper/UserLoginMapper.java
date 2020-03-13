package com.itheima.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.itheima.pojo.User;


public interface UserLoginMapper {
	
	public User queryUserName(@Param("username") String username) ;

	public List<User> userAll(User user);

	
	int queryUserRolesCount(@Param("id")String id);

	int userDel(@Param("id")String id);

	
	public int userAdd(User user);

	public int userRoleAdd(Map<String, Object> map);

	public User userByid(@Param("id") String id);

	
	public String[] userAndrole(@Param("id") String id);

	
	public int userEdit(User user);

	public int delUserRole(Integer id);
	
	
}
