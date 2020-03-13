package com.itheima.interfaces;

import java.util.List;
import java.util.Map;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

public interface RoleService {

	List<Role> queryUserRoles(Integer id);

	PageResult roleAll(Role role, Integer currentPage, Integer pageSize);

	void roleDel(String id);

	Map<String, Object> queryRolePers(String id);

	void roleAdd(Role role);

	void roleEdit(Role role);

	Role roleByid(String id);

	List<String> roleTree(String id);

}
