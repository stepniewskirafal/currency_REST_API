package pl.rstepniewski.demo.service;

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

    @Retryable(value = InvalidCurrentPairException.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public BigDecimal getExchangeRate(String currencyFrom, String currencyTo) {
        try {
            ExchangeRateResponse response = exchangeRateClient.getExchangeRate(currencyFrom, currencyTo);
            return Optional.ofNullable(response)
                    .orElseThrow(() -> new InvalidCurrentPairException("No data available from NBP API"))
                    .getRates().get(0).getMid();
        } catch (Exception e) {
            throw new InvalidCurrentPairException("Unable to retrieve exchange rate for " + currencyFrom + " to " + currencyTo + ": " + e.getMessage());
        }
    }
}
