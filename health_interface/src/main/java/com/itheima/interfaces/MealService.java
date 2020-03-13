package com.itheima.interfaces;

import java.util.List;

import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;

public interface MealService {
	
	
	public PageResult mealAll(Setmeal setmeal,Integer page,Integer size);
	public Setmeal mealByid(Integer id);
	//添加中间表
	public void mealAdd(Setmeal setmeal);
	
	public void mealDel(Integer id);
	
	//先删除套餐的体检组 在添加 
	public void mealUpdate(Setmeal setmeal);
	
	//图片存到redis集合 每次上传的跟 上传成功的
	public void setMealImageCatch(String key,String fileName);
	
	
	//定时删除服务器上的垃圾图片
	public void deleteHisImage();
	
	
	//手机端查询所有套餐
	public Result getAllSetmeal();
	public Result findById(String id);
	public void login(Member member);
	
}
