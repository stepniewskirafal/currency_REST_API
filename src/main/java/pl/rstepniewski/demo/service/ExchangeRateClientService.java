package pl.rstepniewski.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.rstepniewski.demo.dto.ExchangeRateResponse;
import pl.rstepniewski.demo.exception.InvalidCurrentPairException;
import pl.rstepniewski.demo.config.cache.CacheConditionService;
import pl.rstepniewski.demo.exception.UnexpectedFetchExchangeException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateClientService {

    private final ExchangeRateClient exchangeRateClient;
    private final CacheConditionService cacheConditionService;

    public BigDecimal fetchExchangeRate(String currencyFrom, String currencyTo) {
        try {
            ExchangeRateResponse response = exchangeRateClient.getExchangeRate(currencyFrom, currencyTo);
            return Optional.ofNullable(response)
                    .orElseThrow(() -> new InvalidCurrentPairException("No data available from NBP API"))
                    .getRates().get(0).getMid();
        } catch (InvalidCurrentPairException e) {
            log.error("Invalid currency pair provided: {} to {}. Error: {}", currencyFrom, currencyTo, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching exchange rate for: {} to {}. Error: {}", currencyFrom, currencyTo, e.getMessage(), e);
            throw new UnexpectedFetchExchangeException("Unexpected error while fetching exchange rate");
        }
    }
}
