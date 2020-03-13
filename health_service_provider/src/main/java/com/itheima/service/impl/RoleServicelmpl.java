package com.itheima.service.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.entity.PageResult;
import com.itheima.interfaces.RoleService;
import com.itheima.mapper.RoleMapper;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

@Transactional
@Service(interfaceClass=RoleService.class)
public class RoleServicelmpl implements RoleService {

	
	@Autowired
	private RoleMapper rolemapper;
	
	@Override
	public List<Role> queryUserRoles(Integer id) {
		//查询用户的角色 
		List<Role> list=new ArrayList<Role>();
		try {
		list=rolemapper.queryUserRoles(id);
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public PageResult roleAll(Role role, Integer currentPage, Integer pageSize) {
		PageResult pageResult=new PageResult();
		PageInfo<Role> pageInfo=null;
		List<Role> list=null;
		try {
			if (currentPage!=null&&pageSize!=null) {
				PageHelper.startPage(currentPage, pageSize);
				list=rolemapper.roleAll(role);
				pageInfo=new PageInfo<Role>(list);
			}
			else {
				list=rolemapper.roleAll(role);
			}
			
			if (pageInfo!=null) {
				pageResult.setTotal(pageInfo.getTotal());
			}
			
			pageResult.setRows(list);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageResult;
	}

	@Override
	public void roleDel(String id) {
		//查询权限
		int rs=rolemapper.queryRolePer(id);
		if (rs>0) {
			throw new RuntimeException("该角色下面有权限不能删除!");
		}
		//查询菜单
		int rm=rolemapper.queryRoleMenu(id);
		if (rm>0) {
			throw new RuntimeException("该角色下面有菜单不能删除!");
		}
		
		//删除角色 用户中间表 
		rolemapper.delRoleUser(id);
		
		rolemapper.delRole(id);
	}

	@Override
	public Map<String, Object> queryRolePers(String id) {
		Map<String, Object> map=new HashMap<String, Object>();
	List<Permission> list=rolemapper.queryRolePers(id);
	map.put("rows", list);
		List<Integer> stas=new ArrayList<Integer>();
	for (Permission permission : list) {
		if (permission.getStatus()!=0) {
			//选中
			stas.add(permission.getId());
		}
	}
		
	map.put("list", stas);
		
		return map;
	}

	@Override
	public void roleAdd(Role role) {
		rolemapper.roleAdd(role);
		saveRoleAndPreAndMenu(role.getId(),role.getPermissions(),role.getMenus());
		
		
	}
	
	//添加角色 权限 菜单 中间表
	public void saveRoleAndPreAndMenu(Integer rid,Set<Permission> permissions,LinkedHashSet<Menu> menus) {
		if (permissions!=null&&permissions.size()>0) {
			
			
			for (Permission per : permissions) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("rid", rid);
				map.put("perid", per.getId());
				rolemapper.insertRoleAndPer(map);
				
			}
			
			for (Menu menu : menus) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("rid", rid);
				map.put("mid", menu.getId());
				rolemapper.insertRoleAndMenu(map);
			}
			
		}
		
		
		
	}

	@Override
	public void roleEdit(Role role) {
		
		rolemapper.roleEdit(role);
		//删除 
		rolemapper.delRolePer(role.getId()+"");
		rolemapper.delRoleMenu(role.getId()+"");
		
		saveRoleAndPreAndMenu(role.getId(),role.getPermissions(),role.getMenus());
		
		
	}

	@Override
	public Role roleByid(String id) {
		
		
		return rolemapper.roleByid(id);
	}

	@Override
	public List<String> roleTree(String id) {
		
		return rolemapper.roleTree(id);
	}

}
