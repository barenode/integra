package com.integra.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@EnableCaching
public class IntegraConfig {

    public static final String REPORT_CACHE = "reports";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(REPORT_CACHE);
    }

    @Bean
    public MappingJackson2HttpMessageConverter createXmlHttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }
}
