package pl.rstepniewski.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import pl.rstepniewski.demo.exception.InvalidCurrentPairException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final ExchangeRateClientService exchangeRateClientService;
    private final CacheService cacheService;

    @Retryable(value = InvalidCurrentPairException.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public BigDecimal getExchangeRate(String currencyFrom, String currencyTo) {
        String cacheKey = currencyFrom + "_" + currencyTo;

        BigDecimal cachedRate = cacheService.getExchangeRateFromCache(cacheKey);
        if (cachedRate != null) {
            return cachedRate;
        }

        try {
            BigDecimal rate = exchangeRateClientService.fetchExchangeRate(currencyFrom, currencyTo);
            cacheService.putExchangeRateInCache(cacheKey, rate);
            return rate;

        } catch (Exception e) {
            throw new InvalidCurrentPairException("Unable to retrieve exchange rate for " + currencyFrom + " to " + currencyTo + ": " + e.getMessage());
        }
    }
}
