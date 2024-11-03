package pl.rstepniewski.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.rstepniewski.demo.dto.AccountRequest;
import pl.rstepniewski.demo.dto.AccountResponse;
import pl.rstepniewski.demo.dto.ExchangeRequest;
import pl.rstepniewski.demo.model.Account;
import pl.rstepniewski.demo.service.AccountService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountResponse defaultAccountResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        defaultAccountResponse = new AccountResponse(
                new Account("John", "Doe", BigDecimal.valueOf(0.0)));
    }

    @Test
    void shouldCreateAccount() {
        // Given
        AccountRequest request = new AccountRequest();
        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(defaultAccountResponse);

        // When
        ResponseEntity<AccountResponse> result = accountController.createAccount(request);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Expected status code to be OK for account creation");
        assertEquals(defaultAccountResponse, result.getBody(), "Account response body does not match expected value");
    }

    @Test
    void shouldGetAccount() {
        // Given
        String accountId = "123";
        when(accountService.getAccount(accountId)).thenReturn(defaultAccountResponse);

        // When
        ResponseEntity<AccountResponse> result = accountController.getAccount(accountId);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Expected status code to be OK for account retrieval");
        assertEquals(defaultAccountResponse, result.getBody(), "Account response body does not match expected value");
    }

    @Test
    void shouldExchangeCurrency() {
        // Given
        String accountId = "123";
        ExchangeRequest request = new ExchangeRequest();
        when(accountService.exchangeCurrency(accountId, request)).thenReturn(defaultAccountResponse);

        // When
        ResponseEntity<AccountResponse> result = accountController.exchangeCurrency(accountId, request);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Expected status code to be OK for currency exchange");
        assertEquals(defaultAccountResponse, result.getBody(), "Account response body does not match expected value");
    }
}
