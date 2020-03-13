package com.itheima.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.interfaces.CheckGroupService;
import com.itheima.mapper.CheckGroupMapper;
import com.itheima.pojo.CheckGroup;

@Service(interfaceClass=CheckGroupService.class)
@Transactional(propagation=Propagation.REQUIRED)
public class CheckGroupServicelmpl implements CheckGroupService {

	@Autowired
	private  CheckGroupMapper checkGroupMapper;
	
	@Override
	public void checkGroupAdd(CheckGroup checkGroup) {
		int	c=checkGroupMapper.checkGroupAdd(checkGroup);
		if (c<=0) {
				throw new RuntimeException(MessageConstant.ADD_CHECKGROUP_FAIL);
		}
		
		addGroupAndItem(checkGroup.getCheckitemIds(), checkGroup.getId());
		

	}

	@Override
	public PageResult checkGroupAll(CheckGroup checkGroup, Integer currentPage, Integer pageSize) {
		PageResult pageResult=new PageResult();
		List<CheckGroup> list=null;
		PageInfo<CheckGroup> pageInfo=null;
		if (currentPage!=null&&pageSize!=null) {
			
			PageHelper.startPage(currentPage, pageSize);
			 list=checkGroupMapper.checkGroupAll(checkGroup);
			 pageInfo=new PageInfo<>(list);
		}
		else {
			list=checkGroupMapper.checkGroupAll(checkGroup);
		}
		
		if (pageInfo!=null) {
			pageResult.setTotal(pageInfo.getTotal());
		}
		pageResult.setRows(list);
		
		return pageResult;
	}

	@Override
	public void checkGroupDel(Integer id) throws RuntimeException {
		//查询下面的套餐 有套餐不能删除
		/*
		 * int count=checkGroupMapper.selectGroupmealby(id); if (count>0) { throw new
		 * RuntimeException("该检查组下面有套餐不能删除!"); }
		 */
		
		//反正下次修改套餐 查询了组 先删除中间表清空了 反正
		//删除组下面的项关系
		
		checkGroupMapper.deleteGroupitems(id);
		checkGroupMapper.checkGroupDel(id);
	}

	@Override
	public CheckGroup checkGroupByid(Integer id) {
		// TODO Auto-generated method stub
		CheckGroup checkGroup=checkGroupMapper.checkGroupByid(id);
		//查询他的项
		String[] groupItem=checkGroupMapper.selectGroupitem(id);
		checkGroup.setCheckitemIds(groupItem);
		return checkGroup;
	}

	@Override
	public void updateCheckGroup(CheckGroup checkGroup) {
		// 先删除中间表 
		int s=checkGroupMapper.deleteGroupitems(checkGroup.getId());
		
		int count=checkGroupMapper.updateCheckGroup(checkGroup);
		if (count<=0) {
			throw new RuntimeException(MessageConstant.EDIT_CHECKGROUP_FAIL);
		}
		
		addGroupAndItem(checkGroup.getCheckitemIds(), checkGroup.getId());
		
		
	}
	
	//添加组 项中间表 
	public void addGroupAndItem(String[] checkitemIds,int groupId) {
		//循环添加中间表 
	
		if (checkitemIds!=null&&checkitemIds.length>0) {
			for (int i = 0; i < checkitemIds.length; i++) {
				Map<String,Object> checkGroupIteMap=new HashMap<String, Object>();
				checkGroupIteMap.put("checkgroupid", groupId);
				checkGroupIteMap.put("checkitemid", checkitemIds[i]);
				
				int returns=checkGroupMapper.checkItemAndGroupAdd(checkGroupIteMap);
			 //中间表一条势必
				 if (returns<=0) {
					 throw new RuntimeException("添加检查组下面的检查项失败!请重新尝试!");
				 }
			}
			
		}
		
		
		
	}

}
