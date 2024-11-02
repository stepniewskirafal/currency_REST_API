package pl.rstepniewski.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Value("${nbp-api.base-url}")
    private String nbpApiBaseUrl;
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(nbpApiBaseUrl)
                .build();
    }
}
