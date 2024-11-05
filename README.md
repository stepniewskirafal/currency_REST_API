# Banking Application

## 📝 Project Description
A comprehensive banking application that allows users to manage accounts and perform financial transactions utilizing current exchange rates from the National Bank of Poland (NBP) API. The system includes advanced error handling, detailed logging, and efficient caching mechanisms.

## 🛠️ Technologies
- Java 17
- Spring Boot 3.3
- Spring Web
- Spring Data MongoDB
- Maven
- NBP API Integration
- Caffeine Cache
- Lombok
- Docker

## 🏗️ Architecture Features

### Caching System
- Implemented using **Caffeine Cache**.
- Configurable cache settings:
  - Initial capacity: **5 entries**
  - Maximum size: **100 entries**
  - Expiration time: **12 hours**
- Caches exchange rates to minimize API calls and improve performance.

### Logging System
- Leveraged **SLF4J** for comprehensive logging with **Lombok**.
- Log Levels:
  - **ERROR**: For exceptions and critical issues.
  - **INFO**: For significant business events.
  - **DEBUG**: For in-depth debugging insights.

## 🔄 API Endpoints

### Account Operations
1. **Create Account**
  - **POST** `/api/accounts`
2. **Get Account Details**
  - **GET** `/api/accounts/{accountId}`
3. **Exchange Currency**
  - **POST** `/api/accounts/{accountId}/exchange`

## 💾 Data Models

### Account
- `accountId` (String)
- `firstName` (String)
- `lastName` (String)
- `balancePLN` (BigDecimal)
- `balanceUSD` (BigDecimal)

### Exchange Request
- `amount` (BigDecimal)
- `currencyFrom` (String)
- `currencyTo` (String)

## ⚠️ Error Handling

### Business Exceptions
1. **`InsufficientBalanceException`**
  - **Error Code**: `INSUFFICIENT_BALANCE`
  - **Occurs When**: Insufficient funds for the transaction.
  - **HTTP Status**: 400 Bad Request

2. **`AccountNotFoundException`**
  - **Error Code**: `ACCOUNT_NOT_FOUND`
  - **Occurs When**: Account does not exist.
  - **HTTP Status**: 404 Not Found

3. **`InvalidCurrencyPairException`**
  - **Error Code**: `INVALID_CURRENCY_PAIR`
  - **Occurs When**: Invalid currency conversion request.
  - **HTTP Status**: 400 Bad Request

### NBP API Integration Errors
1. **No Data Found** (HTTP 404)
  - **Error Code**: `NOT_FOUND`
  - **Occurs When**: No data available for the requested timeframe.

2. **Invalid Request** (HTTP 400)
  - **Error Code**: `BAD_REQUEST`
  - **Occurs When**: Malformed request or API limit exceeded.

3. **Server Error** (HTTP 500)
  - **Error Code**: `API_ERROR`
  - **Occurs When**: Issues with the NBP API.

## 🔍 Error Response Format
```json
{
  "errorCode": "ERROR_CODE",
  "timestamp": "2024-03-21T10:15:30.123Z"
}
```

## 🐳 Docker Support
- Application is containerized along with MongoDB.
- Docker Compose configurations available for straightforward deployment.
- Separate configurations for development and production environments.

## 🚀 Running the Application

### Prerequisites
- Java 17
- Docker & Docker Compose
- Maven

### Local Development
1. **Start MongoDB**:
   ```bash
   docker-compose -f docker-compose_mongo.yml up -d
   ```
2. **Build the Application**:
   ```bash
   ./mvnw clean package
   ```
3. **Run the Application**:
   ```bash
   ./mvnw spring-boot:run
   ```

### Docker Deployment (to do)
```bash
docker-compose up -d
```

## 🔒 Security Considerations
- **MongoDB Secure Configuration**: Proper user roles and access controls.
- **Environment-based Configurations**: Secure application settings for different environments.
- **Robust Error Handling**: To prevent sensitive information leakage.

## 🔧 Code References

### Global Exception Handler

```java
package pl.rstepniewski.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

@ControllerAdvice(basePackages = "pl.rstepniewski.demo.controller")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        log.error("Insufficient balance error occurred", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "INSUFFICIENT_BALANCE",
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
        log.error("Account not found", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "ACCOUNT_NOT_FOUND",
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidCurrencyPairException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInvalidCurrencyPairException(InvalidCurrencyPairException ex) {
        log.error("Invalid currency pair", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_CURRENCY_PAIR",
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex) {
        ErrorResponse errorResponse;
        HttpStatus status;

        if (ex instanceof HttpStatusCodeException) {
            HttpStatusCodeException httpEx = (HttpStatusCodeException) ex;
            if (httpEx.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("No data found for the specified time range", httpEx);
                errorResponse = new ErrorResponse(
                        "NOT_FOUND",
                        LocalDateTime.now().toString()
                );
                status = HttpStatus.NOT_FOUND;
            } else if (httpEx.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.error("Bad request to API", httpEx);
                errorResponse = new ErrorResponse(
                        "BAD_REQUEST",
                        LocalDateTime.now().toString()
                );
                status = HttpStatus.BAD_REQUEST;
            } else {
                log.error("NBP API error", httpEx);
                errorResponse = new ErrorResponse(
                        "API_ERROR",
                        LocalDateTime.now().toString()
                );
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            log.error("Unexpected API error", ex);
            errorResponse = new ErrorResponse(
                    "API_ERROR",
                    LocalDateTime.now().toString()
            );
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
}
```

### Cache Configuration

```java
package pl.rstepniewski.demo.config;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.expiration.hours}")
    private int cacheExpirationHours;

    @Value("${cache.cache-max-size}")
    private int cacheMaxSize;

    @Value("${cache.cache-init-capacity}")
    private int cacheInitCapacity;

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheExpirationHours, TimeUnit.HOURS)
                .initialCapacity(cacheInitCapacity)
                .maximumSize(cacheMaxSize);
    }

    @Bean
    public Cache<String, BigDecimal> exchangeRateCache(Caffeine<Object, Object> caffeine) {
        return caffeine.build();
    }
}
```

