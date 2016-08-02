package com.spring.applicationContext;

import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Context {
	
	public static void main(String[] args) {
		  FileSystemXmlApplicationContext  context = new FileSystemXmlApplicationContext("classpath*:/conf/opas-*.xml");
		  
		  MessageSource messageSource = (MessageSource) context.getBean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME);
		  
	}

}
