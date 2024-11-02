package pl.rstepniewski.demo.service.strategy;

import pl.rstepniewski.demo.model.Account;
import pl.rstepniewski.demo.service.CurrencyService;

import java.math.BigDecimal;

public interface ExchangeStrategy {
    void exchange(Account account, BigDecimal amount, CurrencyService currencyService);
}