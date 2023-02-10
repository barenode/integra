package com.integra.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class IntegraConfig {

    public static String REPORT_CACHE = "reports";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(REPORT_CACHE);
    }
}
