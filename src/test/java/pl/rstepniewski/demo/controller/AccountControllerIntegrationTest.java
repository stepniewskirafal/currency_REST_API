package pl.rstepniewski.demo.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
class AccountControllerIntegrationTest {
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final BigDecimal INITIAL_BALANCE_PLN = BigDecimal.valueOf(1000.0);
    private static final BigDecimal INITIAL_BALANCE_USD = BigDecimal.ZERO;
    private static final BigDecimal AMOUNT_TO_EXCHANGE_PLN = BigDecimal.valueOf(100.0);
    private static final BigDecimal BIG_AMOUNT_TO_EXCHANGE_PLN = BigDecimal.valueOf(10000.0);
    private static final BigDecimal DECREASED_INITIAL_BALANCE_PLN = BigDecimal.valueOf(900.0);
    private static final String CURRENCY_TO_FROM = "PLN";
    private static final String CURRENCY_TO_TO = "USD";
    private static final String NON_EXISTENT_CURRENCY = "USD";
    private static final int NON_EXISTENT_ACCOUNT_ID = 123;

    @Autowired
    private MockMvc mockMvc;
    private String baseUrl;
    private String requestBody;
    private String accountId;

    @BeforeEach
    void setUp() throws Exception {
        baseUrl = "http://localhost:8080/api/accounts";
        requestBody = String.format(Locale.US, "{\n" +
                "  \"firstName\": \"%s\",\n" +
                "  \"lastName\": \"%s\",\n" +
                "  \"initialBalancePLN\": %.2f\n" +
                "}", FIRST_NAME, LAST_NAME, INITIAL_BALANCE_PLN.doubleValue());
        accountId = createAccount();
    }

    @Test
    void shouldCreateAccountWithInitialValues() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.balancePLN").value(INITIAL_BALANCE_PLN.doubleValue()))
                .andExpect(jsonPath("$.balanceUSD").value(INITIAL_BALANCE_USD.doubleValue()));
    }

    @Test
    void shouldRetrieveAccountDetails() throws Exception {
        String getAccountUrl = baseUrl + "/" + NON_EXISTENT_ACCOUNT_ID;

        mockMvc.perform(get(getAccountUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotRetrieveAccountDetailsWrongAccountId() throws Exception {
        String getAccountUrl = baseUrl + "/" + accountId;

        mockMvc.perform(get(getAccountUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.balancePLN").value(INITIAL_BALANCE_PLN.doubleValue()));
    }

    @Test
    void shouldExchangeCurrencyAndUpdateBalances() throws Exception {
        String exchangeUrl = baseUrl + "/" + accountId + "/exchange";

        mockMvc.perform(post(exchangeUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createExchangeRequest(AMOUNT_TO_EXCHANGE_PLN.doubleValue(), CURRENCY_TO_FROM, CURRENCY_TO_TO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balancePLN").value(DECREASED_INITIAL_BALANCE_PLN.doubleValue()));
    }

    @Test
    void shouldNotExchangeCurrencyAndUpdateBalancesWrongAccountId() throws Exception {
        String exchangeUrl = baseUrl + "/" + NON_EXISTENT_ACCOUNT_ID + "/exchange";

        mockMvc.perform(post(exchangeUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createExchangeRequest(AMOUNT_TO_EXCHANGE_PLN.doubleValue(), CURRENCY_TO_FROM, CURRENCY_TO_TO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotExchangeCurrencyAndUpdateBalancesTooBigAmount() throws Exception {
        String exchangeUrl = baseUrl + "/" + accountId + "/exchange";

        mockMvc.perform(post(exchangeUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createExchangeRequest(BIG_AMOUNT_TO_EXCHANGE_PLN.doubleValue(), CURRENCY_TO_FROM, CURRENCY_TO_TO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotExchangeCurrencyAndUpdateBalancesNonExistentCurrency() throws Exception {
        String exchangeUrl = baseUrl + "/" + accountId + "/exchange";

        mockMvc.perform(post(exchangeUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createExchangeRequest(AMOUNT_TO_EXCHANGE_PLN.doubleValue(), NON_EXISTENT_CURRENCY, CURRENCY_TO_TO)))
                .andExpect(status().isBadRequest());
    }

    private String createAccount() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        return JsonPath.parse(responseContent).read("$.accountId");
    }

    private String createExchangeRequest(double amount, String currencyFrom, String currencyTo) {
        return String.format(Locale.US,"{ \"amount\": %.2f, \"currencyFrom\": \"%s\", \"currencyTo\": \"%s\" }",
                amount, currencyFrom, currencyTo);
    }
}
