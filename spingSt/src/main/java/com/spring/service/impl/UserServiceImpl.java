package com.spring.service.impl;

import com.spring.an.SpringA;
import com.spring.service.UserService;

@SpringA("userService")
public class UserServiceImpl implements UserService{

	public void say(String name) {
		// TODO Auto-generated method stub
		  System.out.println("hello :"+name);
	}

}
