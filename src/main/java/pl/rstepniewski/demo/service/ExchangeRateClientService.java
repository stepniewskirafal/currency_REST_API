package pl.rstepniewski.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rstepniewski.demo.dto.ExchangeRateResponse;
import pl.rstepniewski.demo.exception.InvalidCurrentPairException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateClientService {

    private final ExchangeRateClient exchangeRateClient;

    public BigDecimal fetchExchangeRate(String currencyFrom, String currencyTo) {
        ExchangeRateResponse response = exchangeRateClient.getExchangeRate(currencyFrom, currencyTo);
        return Optional.ofNullable(response)
                .orElseThrow(() -> new InvalidCurrentPairException("No data available from NBP API"))
                .getRates().get(0).getMid();
    }
}
