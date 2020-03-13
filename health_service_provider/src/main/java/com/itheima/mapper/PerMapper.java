package com.itheima.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itheima.pojo.Permission;

public interface PerMapper {

	List<Permission> queryRolePers(@Param("roleids") String string);

	List<Permission> perAll();

	int delPer(String id);

	int delPerRole(String id);

	int perAdd(Permission permission);
	int editAdd(Permission permission);

	Permission perByid(String id);

}
