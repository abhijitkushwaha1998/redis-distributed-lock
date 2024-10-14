package com.cache.provider.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {

    @Bean
    public Jedis jedis() {
        return new Jedis("localhost", 6379);
    }


//    @Bean
//    public RedisClient lettuceClient() {
//        return RedisClient.create("redis://localhost:6379");  // Adjust Redis host and port
//    }

}