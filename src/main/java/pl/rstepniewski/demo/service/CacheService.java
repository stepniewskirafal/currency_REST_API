package pl.rstepniewski.demo.service;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final Cache<String, BigDecimal> exchangeRateCache;

    public BigDecimal getExchangeRateFromCache(String cacheKey) {
        return exchangeRateCache.getIfPresent(cacheKey);
    }

    public void putExchangeRateInCache(String cacheKey, BigDecimal rate) {
        exchangeRateCache.put(cacheKey, rate);
    }
}
