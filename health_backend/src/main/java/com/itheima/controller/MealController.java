package com.itheima.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.interfaces.MealService;
import com.itheima.pojo.Setmeal;
import com.itheima.util.CreateStaticPage;
import com.itheima.util.QiniuUtils;

/*
 * 套餐组
 */
@RestController
public class MealController {
	@Reference(interfaceClass=MealService.class)
	private MealService mealService;
	

	//@Secured({ "ROLE_DBA", "ROLE_ADMIN" })
	  @RequestMapping("/upload")
	    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
	        System.out.println(imgFile);
	        String originalFilename = imgFile.getOriginalFilename();//原始文件名 3bd90d2c-4e82-42a1-a401-882c88b06a1a2.jpg
	        int index = originalFilename.lastIndexOf(".");
	        String extention = originalFilename.substring(index - 1);//.jpg
	        //上傳成功是这个名字
	        String fileName = UUID.randomUUID().toString() + extention;//	FuM1Sa5TtL_ekLsdkYWcf5pyjKGu.jpg
	        try {
	            //将文件上传到七牛云服务器
	            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
	           // jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
	            mealService.setMealImageCatch(MessageConstant.Set_SETMEAL_Image_UpNODB, MessageConstant.UpCom+fileName);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
	        }
	        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
	    }
	  
	
	
	
		@RequestMapping("mealAll")
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		public PageResult mealAll(@RequestBody(required=false) QueryPageBean queryPageBean) {
			PageResult pageResult=null;
			try {
				Setmeal setmeal=new Setmeal();
				if (queryPageBean!=null) {
					setmeal.setCode(queryPageBean.getQueryString());
					 pageResult=mealService.mealAll(setmeal,queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
				}
				else {
					
					 pageResult=mealService.mealAll(setmeal,null,null);
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return pageResult;
		}
		
		@RequestMapping("mealByid")
		public Result mealByid(Integer id) {
			Result result=new Result();
			try {
				Setmeal setmeal=mealService.mealByid(id);
				result.setData(setmeal);
				result.setFlag(true);
				result.setMessage(MessageConstant.QUERY_SETMEAL_SUCCESS);
				
			} catch (RuntimeException e) {
				result.setFlag(false);
				result.setMessage(e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e) {
				result.setFlag(false);
				result.setMessage(MessageConstant.QUERY_SETMEAL_FAIL);
			e.printStackTrace();
			}
			
			return result;
		}
		
		
		
		
		
		@RequestMapping("mealAdd")
		public Result mealAdd(@RequestBody Setmeal setmeal,String checkgroupIds) {
			Result result=new Result();
			try {
				String[] checkgroupIdssz=null;
				if (StringUtils.isNotBlank(checkgroupIds)) {
					checkgroupIdssz=checkgroupIds.split(",");
				}
				setmeal.setCheckgroupIds(checkgroupIdssz);
				mealService.mealAdd(setmeal);
				result.setFlag(true);
				result.setMessage(MessageConstant.ADD_SETMEAL_SUCCESS);
			} catch (RuntimeException e) {
				result.setFlag(false);
				result.setMessage(e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e) {
				result.setFlag(false);
				result.setMessage(MessageConstant.ADD_SETMEAL_FAIL);
				e.printStackTrace();
			}
			
			return result;
		}
		
		
		@RequestMapping("mealDel")
		public Result mealDel(Integer id) {
			Result result=new Result();
			try {
				mealService.mealDel(id);
			
				result.setFlag(true);
				result.setMessage(MessageConstant.DELETE_MEMBER_SUCCESS);
				
				
			} catch (RuntimeException e) {
				result.setFlag(false);
				result.setMessage(e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e) {
				result.setFlag(false);
				result.setMessage(MessageConstant.DELETE_MEMBER_FAIL);
				e.printStackTrace();
			}
			
			return result;
		}
		
		
		//确定修改
		@RequestMapping("mealUpdate")
		public Result mealUpdate(@RequestBody Setmeal setmeal,String checkgroupIds) {
			Result result=new Result();
			
			try {
				String[] checkgroupIdss=null;
				if (StringUtils.isNotBlank(checkgroupIds)) {
					checkgroupIdss=checkgroupIds.split(",");
				}
				setmeal.setCheckgroupIds(checkgroupIdss);
				mealService.mealUpdate(setmeal);
				result.setFlag(true);
				result.setMessage(MessageConstant.EDIT_MEMBER_SUCCESS);
			}
			catch (RuntimeException e) {
				result.setMessage(e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e) {
				result.setMessage(MessageConstant.EDIT_MEMBER_FAIL);
				e.printStackTrace();
			}
			
			return result;
		}
		
		
}
