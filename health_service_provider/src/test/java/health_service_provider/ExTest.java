package health_service_provider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.itheima.HealthServiceProviderApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HealthServiceProviderApplication.class) // 这里的Application是springboot的启动类名
@WebAppConfiguration
public class ExTest {

	
	//读取excel
	@Test
	public void redaExc() throws IOException {
		
	/*	//创建工作簿 
		XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx"); 
		//获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取 
		XSSFSheet sheet = workbook.getSheetAt(0);
	
		for (Row cell : sheet) { 
			//获得单元格中的值 
			for (Cell row : cell) {
				String value = row.getStringCellValue();
				System.out.println(value); 	
			}
			
			}
		}*/
		
		//创建工作簿
		/*
		 * XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
		 * //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取 XSSFSheet sheet = workbook.getSheetAt(0);
		 * //获取当前工作表最后一行的行号，行号从0开始 int lastRowNum = sheet.getLastRowNum(); for(int
		 * i=0;i<=lastRowNum;i++){ //根据行号获取行对象 XSSFRow row = sheet.getRow(i); short
		 * lastCellNum = row.getLastCellNum(); for(short j=0;j<lastCellNum;j++){ String
		 * value = row.getCell(j).getStringCellValue(); System.out.println(value); } }
		 * workbook.close();
		 */
		
		//創建一個 
		//在内存中创建一个Excel文件 
		XSSFWorkbook workbook = new XSSFWorkbook(); 
		//创建工作表，指定工作表名称
		XSSFSheet sheet = workbook.createSheet("传智播客"); //创建行，0表示第一行 
		XSSFRow row = sheet.createRow(0); //创建单元格，0表示第一个单元格
		row.createCell(0).setCellValue("编号"); 
		row.createCell(1).setCellValue("名称"); 
		row.createCell(2).setCellValue("年龄");
		
		XSSFRow row1 = sheet.createRow(1); //创建单元格，0表示第一个单元格
		
		row1.createCell(1).setCellValue("小明"); 
		row1.createCell(2).setCellValue("10"); 
		
		XSSFRow row2 = sheet.createRow(2); 
		row2.createCell(0).setCellValue("2"); 
		row2.createCell(1).setCellValue("小王"); 
		row2.createCell(2).setCellValue("20");
		//通过输出流将workbook对象下载到磁盘 
		FileOutputStream out = new FileOutputStream("D:\\itcast.xlsx"); 
		workbook.write(out); 
		out.flush(); out.close();
		workbook.close();
		
		}
		
	
		
}
