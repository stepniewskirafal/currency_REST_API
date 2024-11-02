package pl.rstepniewski.demo.util;

import pl.rstepniewski.demo.dto.AccountResponse;
import pl.rstepniewski.demo.model.Account;

public class AccountMapper {

    public static AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(account);
    }

    public static Account toAccount(AccountResponse accountResponse) {
        return Account.builder()
                .accountId(accountResponse.getAccountId())
                .firstName(accountResponse.getFirstName())
                .lastName(accountResponse.getLastName())
                .balancePLN(accountResponse.getBalancePLN())
                .balanceUSD(accountResponse.getBalanceUSD())
                .build();
    }
}
