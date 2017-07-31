package com.example.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 集群下session解决方案
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {

}
