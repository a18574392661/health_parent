package com.itheima.service.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.entity.PageResult;
import com.itheima.interfaces.UserLoginService;
import com.itheima.mapper.RoleMapper;
import com.itheima.mapper.UserLoginMapper;
import com.itheima.pojo.User;

@Service(interfaceClass=UserLoginService.class)
@Transactional
public class UserLoginServicelmpl implements UserLoginService {

	
	@Autowired
	private UserLoginMapper userLoginMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	
	
	
	@Override
	public User queryUserName(String username) {
			
		return userLoginMapper.queryUserName(username);
	}



	@Override
	public PageResult userAll(User user, Integer currentPage, Integer pageSize) {
		PageResult pageResult=new PageResult();
		PageInfo<User> pageInfo=null;
		List<User> list=null;
		try {
			PageHelper.startPage(currentPage,pageSize);
			
			 list=userLoginMapper.userAll(user);
			pageInfo=new PageInfo<User>(list);
			pageResult.setRows(list);
			pageResult.setTotal(pageInfo.getTotal());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return pageResult;
	}



	@Override
	public void userDel(String id) {
			//先查询有没有角色 
		int count=userLoginMapper.queryUserRolesCount(id);
		if (count>0) {
			throw new RuntimeException("该用户绑定了角色不能删除!");
		}
		//删除
		int c=userLoginMapper.userDel(id);
	}



	@Override
	public void userAdd(User user) {
		
		int c=userLoginMapper.userAdd(user);
		saveUserRole(user.getId(),user.getRolesz());
		
		
	}



	@Override
	public User userByid(String id) {
		//查询用户
		User user=userLoginMapper.userByid(id);
		//选中的
		String[] roleid=userLoginMapper.userAndrole(id);
		user.setRolesz(roleid);
		return user;
	}

	
	public void saveUserRole(Integer uid,String[] roleids) {
		if (roleids!=null&&roleids.length>0) {
			for (String roleid :roleids) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("userid", uid);
				map.put("roleid", roleid);
				//添加中间表
				userLoginMapper.userRoleAdd(map);
			}
			
		}
		
	}



	@Override
	public void userEdit(User user) {
		userLoginMapper.userEdit(user);
		//删除中间表
		userLoginMapper.delUserRole(user.getId());
		//添加
		this.saveUserRole(user.getId(), user.getRolesz());
		
		
	} 
}
