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

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        // Given
        AccountRequest request = new AccountRequest();
        AccountResponse response = new AccountResponse(
                new Account("John", "Doe", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));
        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(response);

        // When
        ResponseEntity<AccountResponse> result = accountController.createAccount(request);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testExchangeCurrency() {
        // Given
        String accountId = "123";
        ExchangeRequest request = new ExchangeRequest();
        AccountResponse response = new AccountResponse(
                new Account("John", "Doe", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));
        when(accountService.exchangeCurrency(accountId, request)).thenReturn(response);

        // When
        ResponseEntity<AccountResponse> result = accountController.exchangeCurrency(accountId, request);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetAccount() {
        // Given
        String accountId = "123";
        AccountResponse response = new AccountResponse(
                new Account("123", "John", "Doe", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));
        when(accountService.getAccount(accountId)).thenReturn(response);

        // When
        ResponseEntity<AccountResponse> result = accountController.getAccount(accountId);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}