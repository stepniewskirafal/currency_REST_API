package pl.rstepniewski.demo.service;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import pl.rstepniewski.demo.dto.ExchangeRateResponse;
import pl.rstepniewski.demo.exception.InvalidCurrentPairException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final ExchangeRateClient exchangeRateClient;
    private final Cache<String, BigDecimal> exchangeRateCache;

    @Retryable(value = InvalidCurrentPairException.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public BigDecimal getExchangeRate(String currencyFrom, String currencyTo) {
        String cacheKey = currencyFrom + "_" + currencyTo;

        BigDecimal cachedRate = exchangeRateCache.getIfPresent(cacheKey);
        if (cachedRate != null) {
            return cachedRate;
        }

        try {
            ExchangeRateResponse response = exchangeRateClient.getExchangeRate(currencyFrom, currencyTo);
            BigDecimal rate = Optional.ofNullable(response)
                    .orElseThrow(() -> new InvalidCurrentPairException("No data available from NBP API"))
                    .getRates().get(0).getMid();

            exchangeRateCache.put(cacheKey, rate);
            return rate;

        } catch (Exception e) {
            throw new InvalidCurrentPairException("Unable to retrieve exchange rate for " + currencyFrom + " to " + currencyTo + ": " + e.getMessage());
        }
    }
}