package com.example.springboot.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.RedisHelper;
import com.example.springboot.model.User;
import com.example.springboot.service.UserService;

@RestController
public class ApiController {
	
	@Autowired
	private RedisHelper redisHelper;
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		logger.info("index request");
		request.getSession().setAttribute("name", "name");
		return "Hello spring-boot";
	}
	
    @RequestMapping("/hello/{myName}")  
    public String index(@PathVariable String myName) {  
    	logger.info("hello {}", myName);
        return "Hello "+myName+"";  
    }  
    
    @RequestMapping("/redis/set/{value}")
    public String redis(@PathVariable String value){
    	redisHelper.set("spring-boot-redis", value);
    	return "success";
    }
    
    @RequestMapping("/redis/get")
    public String redisGet(){
    	return redisHelper.get("spring-boot-redis");
    }
    
    @RequestMapping("/user/{id}")
    public User user(@PathVariable Long id){
    	return userService.selectByPrimaryKey(id);
    }

}
