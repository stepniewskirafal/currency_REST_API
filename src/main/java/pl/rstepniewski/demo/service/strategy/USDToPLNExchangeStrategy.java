package pl.rstepniewski.demo.service.strategy;

import org.springframework.stereotype.Component;
import pl.rstepniewski.demo.exception.InsufficientBalanceException;
import pl.rstepniewski.demo.model.Account;
import pl.rstepniewski.demo.service.CurrencyService;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class USDToPLNExchangeStrategy implements ExchangeStrategy {
    @Override
    public void exchange(Account account, BigDecimal amount, CurrencyService currencyService) {
        if (!account.canWithdrawUSD(amount)) {
            throw new InsufficientBalanceException("Insufficient balance in USD to complete the exchange.");
        }
        BigDecimal exchangeRate = currencyService.getExchangeRate( "USD", "PLN");
        BigDecimal exchangedAmount = amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
        account.setBalanceUSD(account.getBalanceUSD().subtract(amount));
        account.setBalancePLN(account.getBalancePLN().add(exchangedAmount));
    }
}