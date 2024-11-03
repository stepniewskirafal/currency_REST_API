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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private String baseUrl;
    private String requestBody;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:8080/api/accounts";
        requestBody = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"initialBalancePLN\": 1000.0\n" +
                "}";
    }

    private String createAccount() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        return JsonPath.parse(responseContent).read("$.accountId");
    }

    @Test
    void shouldCreateAccountWithInitialValues() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balancePLN").value(1000.0))
                .andExpect(jsonPath("$.balanceUSD").value(0));
    }

    @Test
    void shouldRetrieveAccountDetails() throws Exception {
        String accountId = createAccount();
        String getAccountUrl = baseUrl + "/" + accountId;

        mockMvc.perform(get(getAccountUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balancePLN").value(1000.0));
    }

    @Test
    void shouldExchangeCurrencyAndUpdateBalances() throws Exception {
        String accountId = createAccount();
        String exchangeUrl = baseUrl + "/" + accountId + "/exchange";

        mockMvc.perform(post(exchangeUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 100.00,\n" +
                                "  \"currencyFrom\": \"PLN\",\n" +
                                "  \"currencyTo\": \"USD\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balancePLN").value(900.00))
                .andExpect(jsonPath("$.balanceUSD").value(24.96));
    }
}
