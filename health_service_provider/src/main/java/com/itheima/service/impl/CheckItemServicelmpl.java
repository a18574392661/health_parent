package com.itheima.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.interfaces.CheckItemService;
import com.itheima.mapper.CheckItemMapper;
import com.itheima.pojo.CheckItem;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServicelmpl implements CheckItemService {

	@Autowired
	private CheckItemMapper checkItemMapper;
	
	@Override
	public void checkAdd(CheckItem checkItem) {
		// TODO Auto-generated method stub
		checkItemMapper.checkAdd(checkItem);
	}

	
	

	@Override
	public PageResult checkAll(@RequestBody CheckItem checkItem, Integer currentPage, Integer pageSize) {
		List<CheckItem> listCheckItems=null;
		PageInfo<CheckItem> pageInfo=null;
		if (currentPage!=null&&pageSize!=null) {
			PageHelper.startPage(currentPage, pageSize);
			listCheckItems=checkItemMapper.checkAll(checkItem);
			pageInfo=new PageInfo<CheckItem>(listCheckItems);
		}
		else {
			listCheckItems=checkItemMapper.checkAll(checkItem);
		}
		
		PageResult pageResult=new PageResult();
		pageResult.setRows(listCheckItems);
		if (pageInfo!=null) {
			pageResult.setTotal(pageInfo.getTotal());
		}
		
	
		return pageResult;
	}




	@Override
	public void checkDel(Integer id) throws RuntimeException{ 
			//先查询中间表
		Integer count=checkItemMapper.selectItemGroupby(id);
		if (count>0) {
			//不能删除
			throw new RuntimeException(MessageConstant.DELETE_CHECKITEM_NOTNULL);
		}
		//删除
		checkItemMapper.checkDel(id);
	}




	@Override
	public CheckItem checkByid(Integer id) {
		CheckItem checkItem=checkItemMapper.checkByid(id);
		if (checkItem==null) {
			//不能删除
			throw new RuntimeException("没有当前检查项目");
		}
		return checkItem;
	}




	@Override
	public void updateCheck(CheckItem checkItem) {
		int c=checkItemMapper.updateCheck(checkItem);
		if (c<=0) {
			throw new RuntimeException(MessageConstant.EDIT_CHECKITEM_FAIL);
		}
		
		
	}




	

}
