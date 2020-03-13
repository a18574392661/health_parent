package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {


	@RequestMapping("main")
	public String main() {
		return "pages/main";
	}
	
  	@RequestMapping("{url}.html")
  	public String to_url(@PathVariable(value="url") String url) {
  		
  		System.out.println(url);
  		return "pages/"+url;
  	}
  	
}
