package com.itheima.mapper;

import java.util.List;
import java.util.Map;

import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;

import tk.mybatis.mapper.common.Mapper;

public interface MealMapper extends Mapper<Setmeal> {
		
	public List<Setmeal> mealAll(Setmeal setmeal);

	public int mealAdd(Setmeal setmeal);
	
	public int mealAndGroupAdd(Map<String,Object> map);

	public int selectMealGroup(Integer id);

	public String[] myalbyIdGroup(int id);

	public Setmeal mealByid(int id);
	
	public int mealGroupDels(int id);
	
	public int mealDel(int id);

	
	public int mealUpdate(Setmeal setmeal);

	public List<Setmeal> queryAllMeal();

//	public Setmeal findById(int parseInt);

	public List<CheckGroup> findGroupByid(Integer id);
	public List<CheckItem> findItemByid(Integer id);

	public Member login(Member member);
	
	
	
	
}
