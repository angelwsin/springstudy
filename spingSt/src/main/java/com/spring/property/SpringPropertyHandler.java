package com.spring.property;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringPropertyHandler {
	
	  public static void main(String[] args) {
		
		  
		  //Spring  路径的解析
		  //1.路径 classpath   classpath*
		  
		  //例子  从文件系统加载
		  FileSystemXmlApplicationContext  sysContext = new FileSystemXmlApplicationContext("classpath*:spring-*.xml");
		  //文件路径的解析  classpath*:spring-*.xml
		  
		  
		  
		
		  
	}

}
