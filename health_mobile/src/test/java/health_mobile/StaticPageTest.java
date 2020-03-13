package health_mobile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.itheima.HealthMobileBackendApplication;




@RunWith(SpringRunner.class)
@SpringBootTest(classes = HealthMobileBackendApplication.class) // 这里的Application是springboot的启动类名
@WebAppConfiguration
public class StaticPageTest {
	
	@Value("${static.path}")
	private String url;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	
	@Test
	public void createPage() throws IOException {
		
		Map<String, Object> map = new HashMap<>();
		map.put("name", "tellsea");
		map.put("age", 20);
		map.put("email", "3210054449@qq.com");
		
		  Context context = new Context();
		    context.setVariables(map);
		    // 输出流
		    File dest = new File(url,"a.html");
		    if (dest.exists()) {
		        dest.delete();
		    }
		    
		    try (
		    		PrintWriter writer = new PrintWriter(dest, "UTF-8")) {
		        // 生成html，第一个参数是thymeleaf页面下的原型名称
		        templateEngine.process("a", context, writer);
		    } catch (Exception e) {
		    	e.printStackTrace();
		      System.out.println("异常");
		    }

	}
	

}
