package com.example.springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.mapper.UserMapper;
import com.example.springboot.model.User;
import com.example.springboot.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User selectByPrimaryKey(Long id){
		return userMapper.selectByPrimaryKey(id);
	}
	
	

}
