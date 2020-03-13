package com.itheima.interfaces;

import com.itheima.entity.PageResult;
import com.itheima.pojo.User;

public interface UserLoginService {

	//根据用户名查询 
	public User queryUserName(String username);

	public PageResult userAll(User user, Integer currentPage, Integer pageSize);

	public void userDel(String id);

	public void userAdd(User user);

	public User userByid(String id);

	public void userEdit(User user);
	
}
