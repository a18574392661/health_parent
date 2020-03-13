package com.itheima.interfaces;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

public interface CheckGroupService {

	
	//添加体检项
		public void checkGroupAdd(CheckGroup checkGroup);
		
		//查询体检项
		public PageResult checkGroupAll(CheckGroup checkGroup,Integer currentPage,Integer pageSize);

		public void checkGroupDel(Integer id) throws RuntimeException;

		public CheckGroup checkGroupByid(Integer id);
		
		public void updateCheckGroup(CheckGroup checkGroup);
		
}
