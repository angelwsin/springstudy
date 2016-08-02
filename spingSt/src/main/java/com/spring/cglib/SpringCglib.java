package com.spring.cglib;

import java.lang.reflect.Method;

import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy.MethodOverrideCallbackFilter;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;

import com.spring.service.impl.UserServiceImpl;

public class SpringCglib {
	
	  public static void main(String[] args) {
		
		  
		  Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(UserServiceImpl.class);
			enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
			enhancer.setCallbackFilter(new CallbackFilter() {
				
				public int accept(Method paramMethod) {
					// TODO Auto-generated method stub
					return 0;
				}
			});
			enhancer.setCallbackTypes(CALLBACK_TYPES);
			return enhancer.createClass();
	}

}
