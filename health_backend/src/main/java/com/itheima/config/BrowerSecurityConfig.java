package com.itheima.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.itheima.logininterfs.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter  {

	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	
	  @Override protected void configure(AuthenticationManagerBuilder auth) throws
	  Exception {
	  
	  System.out.println(auth+"//"); auth.userDetailsService(userDetailsService)
	  .passwordEncoder(new BCryptPasswordEncoder()); }
	 
	@Override
	  protected void configure(HttpSecurity http) throws Exception {
		
		http.headers().frameOptions().disable();
		http.authorizeRequests()

		// 如果有允许匿名的url，填在下面

		 .antMatchers("/to_tree").permitAll()

		.anyRequest().authenticated()

		.and()

		// 设置登陆页 不拦截
			//设置登录的路径 (之前一直以为是登录跳页面)
		.formLogin().loginPage("/login.html").loginProcessingUrl("/login")

		// 设置登陆成功页

		.defaultSuccessUrl("/main").permitAll()
		 .failureUrl("/login.html")
		// 自定义登陆用户名和密码参数，默认为username和password
		 .usernameParameter("/loginError").usernameParameter("username")
		 .passwordParameter("password")

		.and()

		.logout().logoutUrl("/logout").permitAll();

		 

		// 关闭CSRF跨域

		http.csrf().disable();

	  }
	
	
	@Override

	public void configure(WebSecurity web) throws Exception {

	// 设置拦截忽略文件夹，可以对静态资源放行

		web.ignoring().antMatchers("/static/**","/css/**", "/js/**","/bootstrap/**","/image/**","/img/**","/plugins/**");

	}
	
	
}
