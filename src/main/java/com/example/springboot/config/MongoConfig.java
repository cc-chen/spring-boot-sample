package com.example.springboot.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfig {
	
	@Value("${spring.data.mongodb.uri}")
	private String clientUri;

	@Bean
	public MongoTemplate mongoTemplate() {
		MongoClientURI uri = new MongoClientURI(clientUri);
		MongoDbFactory mongoDbFactory;
		try {
			mongoDbFactory = new SimpleMongoDbFactory(uri);
			return new MongoTemplate(mongoDbFactory);
		} catch (UnknownHostException e) {
		}
		return null;
	}

}
