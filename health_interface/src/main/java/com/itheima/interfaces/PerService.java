package com.itheima.interfaces;

import java.util.List;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

public interface PerService {

	List<Permission> queryRolePers(List<Role> listRoles);

	PageResult perAll(QueryPageBean queryPageBean);

	void perDel(String id);

	void perAdd(Permission permission);

	void perEdit(Permission permission);

	Permission perByid(String id);

}
