package com.itheima.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.interfaces.PerService;
import com.itheima.mapper.PerMapper;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

@Transactional
@Service(interfaceClass=PerService.class)
public class PerServicelmpl implements PerService {

	@Autowired
	private PerMapper permapper;
	
	@Override
	public List<Permission> queryRolePers(List<Role> listRoles) {
		 List<Permission> list=null;
		try {
			if (listRoles!=null&&listRoles.size()>0) {
				StringBuilder buffer=new StringBuilder("");
				buffer.append("(");
			for (int i = 0; i < listRoles.size(); i++) {
				buffer.append(listRoles.get(i).getId());//角色id
				if (listRoles.size()-1>i) {
					buffer.append(",");
				}
				
			}
			buffer.append(")");
				
				
		list=permapper.queryRolePers(buffer.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return list;
	}

	@Override
	public PageResult perAll(QueryPageBean queryPageBean) {
	
		PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
		List<Permission> list=permapper.perAll();
		System.out.println(list.size()+"//分页的长度");
		PageInfo<Permission> pageInfo=new PageInfo<Permission>(list);
		PageResult pageResult=new PageResult(pageInfo.getTotal(), list);
		return pageResult;
	}

	@Override
	public void perDel(String id) {
		//删除权限 角色中间表
		permapper.delPerRole(id);
		permapper.delPer(id);
		
	}

	@Override
	public void perAdd(Permission permission) {
		
		permapper.perAdd(permission);
		
	}

	@Override
	public void perEdit(Permission permission) {
		// TODO Auto-generated method stub
		permapper.editAdd(permission);
	}

	@Override
	public Permission perByid(String id) {
		// TODO Auto-generated method stub
		return permapper.perByid(id);
	}

}
