package com.itheima.execute;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.interfaces.MealService;
import com.itheima.service.MealImageTimeService;
import com.itheima.service.impl.MealServicelmpl;
import com.itheima.service.lmpl.MealImageTimeServicelmpl;



public class QuartzDemo implements Job {

	//@Autowired
	//private MealImageTimeService mealImageTimeService=new MealImageTimeServicelmpl();
	
	@Autowired
	@Resource(name="MealImageTimeServicelmpl")
	private MealImageTimeService mealService;
	
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
   // 	System.out.println(mealImageTimeService+"aaaa");
   // mealImageTimeService.deleteHisImage();
    	//mealService.deleteHisImage();
    	System.out.println(mealService);
 
    	
    	
    }

	
}
