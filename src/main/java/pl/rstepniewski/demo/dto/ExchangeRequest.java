package pl.rstepniewski.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequest {
    private BigDecimal amount;
    private String currencyFrom;
    private String currencyTo;

}
