package pl.rstepniewski.demo.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.cache-max-size}")
    private int cacheMaxSize;

    @Value("${cache.cache-init-capacity}")
    private int cacheInitCapacity;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("exchangeRates");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(cacheInitCapacity)
                .maximumSize(cacheMaxSize));

        return cacheManager;
    }
}
