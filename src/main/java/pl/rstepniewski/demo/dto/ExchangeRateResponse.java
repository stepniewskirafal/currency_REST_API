package pl.rstepniewski.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExchangeRateResponse {

    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    @Getter
    @Setter
    public static class Rate {
        private String no;
        private String effectiveDate;
        private BigDecimal mid;
    }
}
