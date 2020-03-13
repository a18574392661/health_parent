package com.itheima.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.interfaces.CheckItemService;
import com.itheima.pojo.CheckItem;


@Controller
public class CheckItemController{

	
	@Reference(interfaceClass = CheckItemService.class)
	private  CheckItemService checkItemService;
	
	@PreAuthorize("hasPermission('add')") //需要有这个权限
  	@RequestMapping("checkAdd")
  	@ResponseBody
  	public Result checkAdd(@RequestBody CheckItem checkItem) {
  		Result result=new Result();
  		try {
  			checkItemService.checkAdd(checkItem);
  			result.setFlag(true);
			result.setMessage(MessageConstant.ADD_CHECKITEM_SUCCESS);
		} catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.ADD_CHECKITEM_FAIL);
			e.printStackTrace();
		}
  		
  		return result;
  		
  	}
  	
  	
	@RequestMapping("checkAll")
  	@ResponseBody
  	public PageResult checkAll(@RequestBody(required=false) QueryPageBean queryPageBean ) {
	
		PageResult pageResult=new PageResult();
  		try {
  			CheckItem checkItem=new CheckItem();//虽然只有一个参数 但也这么写了
  			
  			if (queryPageBean!=null) {
  				checkItem.setCode(queryPageBean.getQueryString());
  				 pageResult=checkItemService.checkAll(checkItem,queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
			}else {
				 pageResult=checkItemService.checkAll(checkItem,null,null);
			}
  			
	  		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
  		
  		return pageResult;
  		
  	}
	
	
	@RequestMapping("checkDel")
	@ResponseBody
  	public Result checkDel(Integer id) {
  		Result result=new Result();
  		try {
  			checkItemService.checkDel(id);
  			result.setFlag(true);
			result.setMessage(MessageConstant.DELETE_CHECKITEM_SUCCESS);
		}
  		catch (RuntimeException e) {
			result.setFlag(false);
			result.setMessage(e.getMessage());
			e.printStackTrace();
		}
  		catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.DELETE_CHECKGROUP_FAIL);
			e.printStackTrace();
		}
  		
  		
  		return result;
  		
  	}
	
	@RequestMapping("checkByid")
	@ResponseBody
	public Result checkByid(Integer id) {
  		Result result=new Result();
  		try {
  			CheckItem checkItem=checkItemService.checkByid(id);
  			result.setData(checkItem);
  			result.setFlag(true);
			result.setMessage(MessageConstant.QUERY_SETMEALLIST_SUCCESS);
		}
  		catch (RuntimeException e) {
			result.setFlag(false);
			result.setMessage(e.getMessage());
			e.printStackTrace();
		}
  		catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.QUERY_CHECKITEM_FAIL);
			e.printStackTrace();
		}
 
  		return result;
  		
  	}
	
	
	@RequestMapping("updateCheck")
	@ResponseBody
	public Result updateCheck(@RequestBody CheckItem checkItem) {
  		Result result=new Result();
  		try {
  			checkItemService.updateCheck(checkItem);
  			result.setFlag(true);
			result.setMessage(MessageConstant.EDIT_CHECKITEM_SUCCESS);
		}
  		catch (RuntimeException e) {
			result.setFlag(false);
			result.setMessage(e.getMessage());
			e.printStackTrace();
		}
  		catch (Exception e) {
			result.setFlag(false);
			result.setMessage(MessageConstant.EDIT_CHECKITEM_FAIL);
			e.printStackTrace();
		}
 
  		return result;
  		
  	}
	
	
}
