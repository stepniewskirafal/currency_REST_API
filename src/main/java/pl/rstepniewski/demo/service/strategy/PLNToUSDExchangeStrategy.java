package pl.rstepniewski.demo.service.strategy;

import org.springframework.stereotype.Component;
import pl.rstepniewski.demo.exception.InsufficientBalanceException;
import pl.rstepniewski.demo.model.Account;
import pl.rstepniewski.demo.service.CurrencyService;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PLNToUSDExchangeStrategy implements ExchangeStrategy {
    @Override
    public void exchange(Account account, BigDecimal amount, CurrencyService currencyService) {
        if (!account.canWithdrawPLN(amount)) {
            throw new InsufficientBalanceException("Insufficient balance in PLN to complete the exchange.");
        }
        BigDecimal exchangeRate = currencyService.getExchangeRate("PLN", "USD");
        BigDecimal exchangedAmount = amount.divide(exchangeRate, 2, RoundingMode.HALF_UP);
        account.setBalancePLN(account.getBalancePLN().subtract(amount));
        account.setBalanceUSD(account.getBalanceUSD().add(exchangedAmount));
    }
}