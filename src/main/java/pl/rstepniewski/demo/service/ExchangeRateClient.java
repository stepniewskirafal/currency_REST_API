package pl.rstepniewski.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pl.rstepniewski.demo.dto.ExchangeRateResponse;

@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    private final RestClient restClient;

    @Value("${nbp-api.exchange-rate-path-usd}")
    private String endpoint;

    public ExchangeRateResponse getExchangeRate(String currencyFrom, String currencyTo) throws RestClientException {
        return restClient.get()
                .uri(endpoint)
                .retrieve()
                .body(ExchangeRateResponse.class);
    }
}