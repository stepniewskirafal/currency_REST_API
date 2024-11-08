package pl.rstepniewski.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.rstepniewski.demo.dto.AccountRequest;
import pl.rstepniewski.demo.dto.AccountResponse;
import pl.rstepniewski.demo.dto.ExchangeRequest;
import pl.rstepniewski.demo.exception.AccountNotFoundException;
import pl.rstepniewski.demo.exception.NegativeInitialBalanceException;
import pl.rstepniewski.demo.model.*;
import pl.rstepniewski.demo.repository.AccountRepository;
import org.springframework.stereotype.Service;
import pl.rstepniewski.demo.service.strategy.ExchangeStrategy;
import pl.rstepniewski.demo.service.strategy.ExchangeStrategyFactory;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;
    private final ExchangeStrategyFactory exchangeStrategyFactory;

    public AccountResponse createAccount(AccountRequest request) {
        if (request.getInitialBalancePLN().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeInitialBalanceException("Initial balance cannot be negative");
        }
        String accountId = UUID.randomUUID().toString();
        Account account = new Account(accountId, request.getFirstName(), request.getLastName(), request.getInitialBalancePLN(), BigDecimal.ZERO);
        accountRepository.save(account);
        return new AccountResponse(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AccountResponse exchangeCurrency(String accountId, ExchangeRequest request) {
        Account account = retrieveAccountWithValidation(accountId);
        ExchangeStrategy exchangeStrategy = getExchangeStrategy(request);
        executeCurrencyExchange(account, request, exchangeStrategy);
        saveUpdatedAccount(account);

        return new AccountResponse(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountId) {
        Account account = getAccountById(accountId);
        return new AccountResponse(account);
    }

    private Account getAccountById(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found."));
    }

    private Account retrieveAccountWithValidation(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found."));
    }

    private ExchangeStrategy getExchangeStrategy(ExchangeRequest request) {
        return exchangeStrategyFactory.getStrategy(
                request.getCurrencyFrom(),
                request.getCurrencyTo()
        );
    }

    private void executeCurrencyExchange(Account account, ExchangeRequest request, ExchangeStrategy exchangeStrategy) {
        exchangeStrategy.exchange(account, request.getAmount(), currencyService);
    }

    private void saveUpdatedAccount(Account account) {
        accountRepository.save(account);
    }
}
