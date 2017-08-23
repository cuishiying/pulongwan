package com.shanglan.pulongwan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@PropertySource("classpath:pulongwan-core.properties")
public class SpringRedisConfig {

	@Autowired
	private Environment environment;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		String hostName = environment.getProperty("redis.host", "localhost");
		int port = Integer.valueOf(environment.getProperty("redis.port", "6379"));
		factory.setHostName(hostName);
		factory.setPort(port);
		return factory;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
		return template;
	}
}
