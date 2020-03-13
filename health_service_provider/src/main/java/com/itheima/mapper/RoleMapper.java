package com.itheima.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

public interface RoleMapper {

	List<Role> queryUserRoles(@Param("uid")Integer id);

	List<Role> roleAll(Role role);

	
	int queryRolePer(@Param("rid")String id);
	int queryRoleMenu(@Param("rid")String id);

	int delRoleUser(@Param("rid")String id);

	int delRole(@Param("rid")String id);

	List<Permission> queryRolePers(@Param("id")String id);

	int insertRoleAndPer(Map<String, Object> map);

	int insertRoleAndMenu(Map<String, Object> map);

	int roleAdd(Role role);
	
	
	int delRolePer(@Param("rid")String id);
	int delRoleMenu(@Param("rid")String id);

	int roleEdit(Role role);

	Role roleByid(String id);

	List<String> roleTree(String id);
	

}
