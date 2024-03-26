package com.hasd.ddcodeboot.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/12/21 10:05
 **/


@Configuration
@EnableCaching
public class CacheConfig {

//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.builder(redisConnectionFactory).build();
//    }
}
