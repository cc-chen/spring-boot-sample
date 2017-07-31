package com.example.springboot.service;

import com.example.springboot.model.User;

public interface UserService {

	User selectByPrimaryKey(Long id);

}
