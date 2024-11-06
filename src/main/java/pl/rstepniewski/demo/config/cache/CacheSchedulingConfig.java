package pl.rstepniewski.demo.config.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "cache.scheduling.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class CacheSchedulingConfig {
    private final CacheManager cacheManager;
    private final CacheConditionService cacheConditionService;

    @Scheduled(cron = "${cache.nbp.update-window.clear-cron}")
    public void clearCacheDuringUpdateWindow() {
        if (cacheConditionService.isWithinUpdateWindow()) {
            Cache exchangeRatesCache = cacheManager.getCache("exchangeRates");
            if (exchangeRatesCache != null) {
                exchangeRatesCache.clear();
            }
        }
    }
}