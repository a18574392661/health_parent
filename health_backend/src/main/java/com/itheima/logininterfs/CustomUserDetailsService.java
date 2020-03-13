package com.itheima.logininterfs;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.itheima.interfaces.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.interfaces.UserLoginService;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

@Service
public class CustomUserDetailsService implements UserDetailsService  {

	
	
	//用户
	@Reference(interfaceClass=UserLoginService.class)
	private UserLoginService userLoginService;
	
	//角色
	@Reference(interfaceClass=RoleService.class)
	private RoleService roleService;
	
	//权限
	@Reference(interfaceClass=PerService.class)
	private PerService perService;
	
	
	//菜单
	@Reference(interfaceClass=MenuService.class)
	private MenuService menuService;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 System.out.println("进入");
		User user=null;
		UserDetails userDetails=null;
		try {
			System.out.println(userLoginService);
			 user=userLoginService.queryUserName(username);
			 if (user==null) {
				return null;//认证失败
			}
			 //查询当前用户的角色 跟权限
			 List<GrantedAuthority> list=new ArrayList<GrantedAuthority>();
			 //根据用户id查询 角色 权限 中间表 
			 List<Role> listRoles=roleService.queryUserRoles(user.getId());
			 //根据用户角色查询 权限
			 List<Permission> permissionlist= perService.queryRolePers(listRoles);
			 for (Role role : listRoles) {
				 list.add(new SimpleGrantedAuthority(role.getKeyword()));
			 }
			 for (Permission per : permissionlist) {
				 list.add(new SimpleGrantedAuthority(per.getKeyword()));
			}
			 
			 user.setRoles(listRoles);
			 
			 

			 userDetails=new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),list);
			 //查询用户信息 
			 
			 
			 System.out.println("成功");
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		
		}
		//拿到用户名查询数据库
		return userDetails;
	}

}
