package pl.rstepniewski.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rstepniewski.demo.service.AccountService;
import pl.rstepniewski.demo.dto.AccountRequest;
import pl.rstepniewski.demo.dto.AccountResponse;
import pl.rstepniewski.demo.dto.ExchangeRequest;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PostMapping("/{accountId}/exchange")
    public ResponseEntity<AccountResponse> exchangeCurrency(@PathVariable String accountId, @RequestBody ExchangeRequest request) {
        return ResponseEntity.ok(accountService.exchangeCurrency(accountId, request));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(accountService.getAccount(accountId));
    }

}
