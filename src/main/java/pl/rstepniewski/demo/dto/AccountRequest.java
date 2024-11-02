package pl.rstepniewski.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {
    private String firstName;
    private String lastName;
    private BigDecimal initialBalancePLN;

}
