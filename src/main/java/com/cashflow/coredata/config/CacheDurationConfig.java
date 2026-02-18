package com.cashflow.coredata.config;

import com.cashflow.coredata.utils.constants.cache.CacheNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

@Configuration
public class CacheDurationConfig {

    @Value("${cache.duration.categories}")
    private Long categoriesCacheDuration;

    @Bean
    public Map<String, Duration> cacheDurations() {
        return Map.of(
                CacheNames.CATEGORIES, Duration.ofMinutes(categoriesCacheDuration)
        );
    }

}
