package pl.rstepniewski.demo.config;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.expiration.hours}")
    private int cacheExpirationHours;

    @Value("${cache.cache-max-size}")
    private int cacheMaxSize;

    @Value("${cache.cache-init-capacity}")
    private int cacheInitCapacity;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("exchangeRates");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(cacheExpirationHours, TimeUnit.HOURS)
                .initialCapacity(cacheInitCapacity)
                .maximumSize(cacheMaxSize));

        return cacheManager;
    }
}
