package pl.rstepniewski.demo.config;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.expiration.hours}")
    private short cacheExpirationHours;

    @Value("${cache.cache-max-size}")
    private short cacheMaxSize;

    @Value("${cache.cache-init-capacity}")
    private short cacheInitCapacity;

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheExpirationHours, TimeUnit.HOURS)
                .initialCapacity(cacheInitCapacity)
                .maximumSize(cacheMaxSize);
    }

    @Bean
    public Cache<String, BigDecimal> exchangeRateCache(Caffeine<Object, Object> caffeine) {
        return caffeine.build();
    }
}
