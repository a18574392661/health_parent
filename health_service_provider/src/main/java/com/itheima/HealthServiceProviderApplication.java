package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;



@SpringBootApplication
@MapperScan(value="com.itheima.mapper")
@ComponentScan(value = "com.itheima")
public class HealthServiceProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthServiceProviderApplication.class, args);
	}

}
