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

    @ExceptionHandler(InvalidCurrentPairException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInvalidCurrentPairException(InvalidCurrentPairException ex) {
        log.error("Invalid currency pair", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_CURRENT_PAIR",
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
