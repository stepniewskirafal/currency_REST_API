package pl.rstepniewski.demo.dto;

import lombok.Data;
import pl.rstepniewski.demo.model.Account;

import java.math.BigDecimal;

@Data
public class AccountResponse {
    private String accountId;
    private String firstName;
    private String lastName;
    private BigDecimal balancePLN;
    private BigDecimal balanceUSD;

    public AccountResponse(Account account) {
        this.accountId = account.getAccountId();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.balancePLN = account.getBalancePLN();
        this.balanceUSD = account.getBalanceUSD();
    }
}
