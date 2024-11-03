package pl.rstepniewski.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "accounts")
public class Account {
    @Id
    private String accountId;
    private String firstName;
    private String lastName;
    private BigDecimal balancePLN;
    private BigDecimal balanceUSD;

    @Version
    private Long version;

    public Account(String firstName, String lastName, BigDecimal balancePLN) {
        this.accountId = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.balancePLN = balancePLN;
    }

    public Account(String accountId, String firstName, String lastName, BigDecimal balancePLN, BigDecimal balanceUSD) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balancePLN = balancePLN;
        this.balanceUSD = balanceUSD;
    }

    public boolean canWithdrawPLN(BigDecimal amount) {
        return balancePLN.compareTo(amount) >= 0;
    }

    public boolean canWithdrawUSD(BigDecimal amount) {
        return balanceUSD.compareTo(amount) >= 0;
    }
}
