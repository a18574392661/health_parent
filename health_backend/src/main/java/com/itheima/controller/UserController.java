package com.itheima.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.interfaces.MenuService;
import com.itheima.interfaces.UserLoginService;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.User;

@Controller
public class UserController {

	
	
	@RequestMapping("login.html")
	public String to_login() {
		BCryptPasswordEncoder passcode=new BCryptPasswordEncoder();
		System.out.println(passcode.encode("123"));
		System.out.println(passcode.matches("123",passcode.encode("123")));
		return "login";
	}
	
	
	@RequestMapping("loginError")
	public String loginError() {
		
		
		return "loginError";
	}
	
	@RequestMapping("getUserNames")
	@ResponseBody
	public Result getUserName() {
		Result result=new Result();
		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			org.springframework.security.core.userdetails.User user=(org.springframework.security.core.userdetails.User) authentication.getPrincipal();
			
			result.setData(user);
			result.setFlag(true);
		} catch (Exception e) {
		
			result.setFlag(false);
			e.printStackTrace();
		}
		return result;
	}
	
	@Reference(interfaceClass=MenuService.class)
	private MenuService menuService;
	
	@Reference(interfaceClass=UserLoginService.class)
	private UserLoginService userLoginService;
	
	@RequestMapping("to_tree")
	@ResponseBody
	public Result to_tree(){
		List<Menu> userMenus=new ArrayList<Menu>();
		Result result=new Result();
		try {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			org.springframework.security.core.userdetails.User user=(org.springframework.security.core.userdetails.User) authentication.getPrincipal();
			Menu root=null;
			List<Menu> list=menuService.queryUserMenu("admin");
			Map<Integer, Menu> data=new HashMap<Integer, Menu>();
			for (Menu permission : list) {
				data.put(permission.getId(), permission);
			}	
			int i=0;
			for (Menu menu : list) {
				if (menu.getParentMenuId()==null) {
					root=menu;
					if (i==0) {
						userMenus.add(root);
					}else {
						if (list.contains(root)) {
							userMenus.add(root);
						}
					}
					i++;
				}else {
					Menu parent	=data.get(menu.getParentMenuId());
					parent.getChildren().add(menu);	
				}
			}
		
			result.setData(userMenus);
			result.setFlag(true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setFlag(false);
		}
	
		//data(root);
		return result;
		
	}
	
	public static void main(String[] args) {
		List<Menu> list=new ArrayList<Menu>();
		Menu menu=new Menu();
		menu.setId(1);menu.setName("aaa");menu.setPath("path");
		Menu menu2=new Menu();
		menu2.setId(2);menu2.setName("aab");menu2.setPath("path2");
		list.add(menu);list.add(menu2);
		
	
		Menu menu3=new Menu();
		menu3.setId(1);menu3.setName("aaa");menu3.setPath("pat44");
		System.out.println(list.contains(menu3));
	}
	
	@ResponseBody
	@RequestMapping("userAll")
	public PageResult userAll(@RequestBody(required=false)QueryPageBean queryPageBean) {
	
		User user=new User();
		user.setRemark(queryPageBean.getRid());
		user.setUsername(queryPageBean.getQueryString());
		PageResult pageResult=userLoginService.userAll(user,queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
		
		return pageResult;
	}
	
	
	@ResponseBody
	@RequestMapping("userDel")
	public Result userDel(String id) {
		Result result=new Result();
		
		try {
			userLoginService.userDel(id);
			result.setFlag(true);
			result.setMessage(MessageConstant.DEL_USER_SUCESS);
		} 
		catch (RuntimeException e) {
			result.setFlag(false);
			result.setMessage(e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.DEL_USER_ERROR);
			e.printStackTrace();
		}
		
		return result;
	}
	
	@RequestMapping("userAdd")
	@ResponseBody
	public Result userAdd(@RequestBody(required=false) User user,String roleids) {
		Result result=new Result();
		try {
			String[] rolesz=null;
			if (StringUtils.isNotBlank(roleids)) {
				rolesz=roleids.split(",");
				
			}
			user.setRolesz(rolesz);
			userLoginService.userAdd(user);
			result.setFlag(true);
			result.setMessage(MessageConstant.ADD_USER_SUCESS);
			
		} catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.ADD_USER_ERROR);
			e.printStackTrace();
		}
		
		return result;
	}
	

	@RequestMapping("userByid")
	@ResponseBody
	public Result userByid(String id) {
		Result result=new Result();
		try {
			
			result.setFlag(true);
			result.setMessage(MessageConstant.EDIT_USER_SUCESS);
			result.setData(userLoginService.userByid(id));
		} catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.EDIT_USER_ERROR);
			e.printStackTrace();
		}
		return result;
		
		
	}
	
	
	@RequestMapping("userEdit")
	@ResponseBody
	public Result userEdit(@RequestBody User user,String roleids) {
		Result result=new Result();
		try {
			
			result.setFlag(true);
			result.setMessage(MessageConstant.EDIT_USER_SUCESS);
			String[] rolesz=null;
			if (StringUtils.isNotBlank(roleids)) {
				rolesz=roleids.split(",");
				
			}
			user.setRolesz(rolesz);
			
			userLoginService.userEdit(user);
		} catch (Exception e) {
			// TODO: handle exception
			result.setFlag(false);
			result.setMessage(MessageConstant.EDIT_USER_ERROR);
			e.printStackTrace();
		}
		return result;
	}
	
	
}
