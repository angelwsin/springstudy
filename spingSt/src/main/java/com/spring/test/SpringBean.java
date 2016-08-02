package com.spring.test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

import com.spring.service.UserService;

public class SpringBean {
	
	 public static void main(String[] args) {
		 
		 //资源的定位
		 ClassPathResource  resource = new ClassPathResource("applicationContext-context.xml");
		 DefaultListableBeanFactory beanFacotry = new DefaultListableBeanFactory();
		  XmlBeanDefinitionReader  reader =  new XmlBeanDefinitionReader(beanFacotry);
		  reader.loadBeanDefinitions(resource);
		  UserService  userService =(UserService) beanFacotry.getBean("userService");
		  userService.say("zhangsan");
		  
	
	}

}
