package com.meme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class RedisConfig {
    
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    // !! This is required for using docker compose with redis image
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("Initializing Redis connection factory...");
        try {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName("redis");
            config.setPort(6379);
            
            LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
            // Make sure to initialize the factory
            factory.afterPropertiesSet();
            
            log.info("Redis connection factory initialized successfully");
            return factory;
        } catch (Exception e) {
            log.error("Failed to initialize Redis connection factory: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("Initializing Redis template...");
        try {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(connectionFactory);
            
            // Use StringRedisSerializer for keys
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashValueSerializer(new StringRedisSerializer());
            
            // Initialize the template
            redisTemplate.afterPropertiesSet();
            
            log.info("Redis template initialized successfully");
            return redisTemplate;
        } catch (Exception e) {
            log.error("Failed to initialize Redis template: {}", e.getMessage(), e);
            throw e;
        }
    }
}
