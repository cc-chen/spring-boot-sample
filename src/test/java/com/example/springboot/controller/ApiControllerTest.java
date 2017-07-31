package com.example.springboot.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationConnect;

	@Before
	public void setUp() throws JsonProcessingException {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();

	}

	@Test
	public void testIndex() {
	}

	@Test
	public void testIndexString() {
	}

	@Test
	public void testRedis() {
	}

	@Test
	public void testRedisGet() {
	}

	@Test
	public void testUser() throws Exception {
		String uri = "/user/1";  
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))  
                .andReturn();  
        int status = mvcResult.getResponse().getStatus();  
        String content = mvcResult.getResponse().getContentAsString();  
        Assert.assertTrue("错误，正确的返回值为200", status == 200);  
        System.out.println(content);
	}

}
