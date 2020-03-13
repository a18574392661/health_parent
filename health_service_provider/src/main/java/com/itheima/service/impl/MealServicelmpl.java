package com.itheima.service.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.interfaces.MealService;
import com.itheima.mapper.MealMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;
import com.itheima.util.CreateStaticPage;
import com.itheima.util.ListUtils;
import com.itheima.util.RedisUtil;

import redis.clients.jedis.Jedis;

@Service(interfaceClass=MealService.class)
@Transactional
public class MealServicelmpl implements MealService {

	@Autowired
	private	MealMapper mealMapper;
	
	@Autowired
	public OrderMapper orderMapper;
	@Autowired
	RedisUtil redisUtil;
	
	@Override
	public PageResult mealAll(Setmeal setmeal, Integer page, Integer size) {
		List<Setmeal> list=null;
		PageInfo<Setmeal> pageInfo=null;
		PageResult pageResult=new PageResult();
		if (page!=null&&size!=null) {
			PageHelper.startPage(page, size);
			list=mealMapper.mealAll(setmeal);
			 pageInfo=new PageInfo<Setmeal>(list);
		}
		else {
			list=mealMapper.mealAll(setmeal);
		}
		
		if (pageInfo!=null) {
			pageResult.setTotal(pageInfo.getTotal());
		}
		pageResult.setRows(list);
		
		return pageResult;
	}

	@Override
	public Setmeal mealByid(Integer id) {
		//Setmeal setmeal=mealMapper.
		
		Setmeal stSetmeal=mealMapper.mealByid(id);
		//查询套餐下面的体检组 
		String[] checkgroupIds=mealMapper.myalbyIdGroup(id);
		stSetmeal.setCheckgroupIds(checkgroupIds);
		return stSetmeal;
	}

	@Override
	public void mealAdd(Setmeal setmeal) {
		
		//添加一条数据 
		int c=mealMapper.mealAdd(setmeal);
		//int c=mealMapper.insert(setmeal);
		if (c<=0) {
			throw new RuntimeException(MessageConstant.ADD_MEMBER_FAIL);
		}
		
		//添加中间表
		mealAndGroupAdd(setmeal.getCheckgroupIds(),setmeal.getId());
		String imageUrl= MessageConstant.UpCom+setmeal.getImg();

		//图片路径保存到redis已经存在的数据库
		this.setMealImageCatch(MessageConstant.Set_SETMEAL_Image_UpDB, imageUrl);
		
		
	}
	
	
	@Value("${static.path}")
	String url;
	
	@Autowired
	private CreateStaticPage createStaticPage;
	
	//测试生成静态页面 查询所有 静态页面生成
	public List<Setmeal> staticMath() {
		List<Setmeal> list=mealMapper.mealAll(new Setmeal());
		Map msMap=new HashMap();
		msMap.put("setmealList", list);	
		createStaticPage.createPage(url, "ModelPage/setmeal", "setmeal.html", msMap);
		return list;
	}
	//增加修改生成详细页面
	public void staticMathByid(List<Setmeal> list) {
		if (list!=null&&list.size()>0) {
			for (Setmeal setmeal : list) {
				//应该要查询多表关联 偷懒了
				Result setmeal2=this.findById(setmeal.getId()+"");
				Map msMap=new HashMap();
				msMap.put("setmeal", (Setmeal)setmeal2.getData());	
				createStaticPage.createPage(url, "ModelPage/setmeal_detail", "setmeal"+setmeal.getId()+".html", msMap);		
		
			}
			
		}
	
		
	}

	@Override
	public void mealDel(Integer id) {
		//先查询有没有组 可以修改没有
		int count=mealMapper.selectMealGroup(id);
		if (count>0) {
			throw new RuntimeException("该套餐下面有组合不能删除!");
		}
		
		mealMapper.mealDel(id);
		
		//页面静态化 生成查询所有页面 
				List<Setmeal> list=staticMath();
				staticMathByid(list);
	}

	@Override
	public void mealUpdate(Setmeal setmeal) {
		int c=mealMapper.mealUpdate(setmeal);
		if (c<=0) {
			throw new RuntimeException(MessageConstant.EDIT_MEMBER_FAIL);
		}
		//删除中间表 在添加
		mealMapper.mealGroupDels(setmeal.getId());
		
		this.mealAndGroupAdd(setmeal.getCheckgroupIds(),setmeal.getId());
		String imageUrl= MessageConstant.UpCom+setmeal.getImg();
		//重新传的图片上传到redis set集合去重复 
	
		//其实都没必要 只要上传名字过去就行
		this.setMealImageCatch(MessageConstant.Set_SETMEAL_Image_UpDB, imageUrl);
		
		
				
	}
	
	//添加套餐 组的中间表
	public void mealAndGroupAdd(String[] checkgroupIds,Integer id) {
		if (checkgroupIds!=null&&checkgroupIds.length>0) {
			for (int i = 0; i < checkgroupIds.length; i++) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("setmealid",id);
				map.put("checkgroupid", checkgroupIds[i]);
				int c=mealMapper.mealAndGroupAdd(map);
				if (c<=0) {
					//回滚事务
					throw new RuntimeException("添加套餐的体检组失败!");
					
				}
			}
			
			
		}	
		
		//页面静态化 生成查询所有页面 
				List<Setmeal> list=staticMath();
				staticMathByid(list);
	}

	@Override
	public void setMealImageCatch(String key, String fileName) {
		 Jedis jedis=redisUtil.getJedis();
		 try {
			jedis.sadd(key, fileName);
			 
			 
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		 
	}
	
	public static void main(String[] args) {
		  Set<String> result = new HashSet<String>();
	        Set<String> set1 = new HashSet<String>() {
	            {
	                add("王者荣耀");
	                add("英雄联盟");
	                add("穿越火线");
	                add("地下城与勇士");
	            }   
	        };

	        Set<String> set2 = new HashSet<String>() {
	            {
	                add("王者荣耀");
	                add("地下城与勇士");
	                add("魔兽世界");
	            }
	        };

	        result.clear();
	        result.addAll(set1);
	        result.retainAll(set2);
	        System.out.println("交集：" + result); //王者地下

	        result.clear();
	        result.addAll(set1);
	        result.removeAll(set2);
	        System.out.println("差集：" + result); //英雄 穿越

	        result.clear();
	        result.addAll(set1);
	        result.addAll(set2);
	        System.out.println("并集：" + result);//王者地下


	}

	@Override
	public void deleteHisImage() {
		Jedis jedis=redisUtil.getJedis();
		System.out.println(jedis);
		try {
			 //拿到非数据库图片 数据库图片 
			 Set<String> notDbImage=jedis.smembers(MessageConstant.Set_SETMEAL_Image_UpNODB);
			 Set<String> DbImage=jedis.smembers(MessageConstant.Set_SETMEAL_Image_UpDB);
			 if (notDbImage!=null&&notDbImage.size()>0&&DbImage!=null&&DbImage.size()>0) {
				
				 
				 Set<String> chaImage=ListUtils.setCha(notDbImage, DbImage);
				 for (String url : chaImage) {
					 System.out.println(url);
					 String oo=url.substring(url.lastIndexOf("/",url.lastIndexOf("/"))+1);
					 //删除图片之后缓存 删除这个 以免总是去定时删除没有的图
					// QiniuUtils.deleteFileFromQiniu(oo);
					 //jedis.srem(MessageConstant.Set_SETMEAL_Image_UpNODB, url);
				}
				 
			}
			
			
			
		} catch (Exception e) {
		 e.printStackTrace();
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		
		
	}


	
	
	@Override
	public Result getAllSetmeal() {
		Jedis jedis=redisUtil.getJedis();
		String key=MessageConstant.GET_MEAL_ALL;
		Result result=new Result();
		List<Setmeal> list=null;
		try {
			String mealAllValue=jedis.get(key);
			if (StringUtils.isNotBlank(mealAllValue)) {
				list=JSON.parseArray(mealAllValue, Setmeal.class);
				
			}
			else {
				//数据库里面查询
				list=mealMapper.queryAllMeal();
				jedis.set(key, JSON.toJSONString(list));
			}
			result.setData(list);
			result.setFlag(true);
		} catch (Exception e) {
			result.setFlag(false);
			e.printStackTrace();
			
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		return result;
	}

	@Override
	public Result findById(String id) {
		Jedis jedis=redisUtil.getJedis();
		String key=MessageConstant.GET_MEAL_FINDID+":"+id;
		Result result=new Result();
		Setmeal setmeal=null;
		try {
			String mealByIdValue=jedis.get(key);
			if (StringUtils.isNotBlank(mealByIdValue)) {
				setmeal=JSON.parseObject(mealByIdValue, Setmeal.class);
				
			}
			else {
				//数据库里面查询
				setmeal=mealMapper.mealByid(Integer.parseInt(id));
				//查询他的套餐组 
			List<CheckGroup> checkGroups=mealMapper.findGroupByid(setmeal.getId());
			
			for (CheckGroup checkGroup : checkGroups) {
				//查询他的项
				List<CheckItem> checkItems=mealMapper.findItemByid(checkGroup.getId());
				checkGroup.setCheckItems(checkItems);
			}
			
			setmeal.setCheckGroups(checkGroups);
				jedis.set(key, JSON.toJSONString(setmeal));
			}
			result.setData(setmeal);
			result.setFlag(true);
		} catch (Exception e) {
			result.setFlag(false);
			e.printStackTrace();
			
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		return result;
	}

	@Override
	public void login(Member member) {
		 //先查询 存在返回 不存在创建 身份证跟手机号码绑定
		Member ms=mealMapper.login(member);
		if (ms==null) {
			//添加 
			int c=orderMapper.numberSave(member);
			
			if (c<=0) {
				throw new RuntimeException(MessageConstant.UMS_LOGIN_FAIT);
			}
		}
		
		
	}

}
