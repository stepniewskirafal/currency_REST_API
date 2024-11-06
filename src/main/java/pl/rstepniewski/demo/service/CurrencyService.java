package pl.rstepniewski.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import pl.rstepniewski.demo.exception.InvalidCurrentPairException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final ExchangeRateClientService exchangeRateClientService;

    @Cacheable(value = "exchangeRates", key = "#currencyFrom + '_' + #currencyTo")
    @Retryable(value = InvalidCurrentPairException.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public BigDecimal getExchangeRate(String currencyFrom, String currencyTo) {
        try {
            return exchangeRateClientService.fetchExchangeRate(currencyFrom, currencyTo);
        } catch (Exception e) {
            throw new InvalidCurrentPairException("Unable to retrieve exchange rate for " + currencyFrom + " to " + currencyTo + ": " + e.getMessage());
        }
    }
}
