package pl.rstepniewski.demo.exception;

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
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
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
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "ACCOUNT_NOT_FOUND",
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidCurrentPairException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInvalidCurrentPairException(InvalidCurrentPairException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
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
                errorResponse = new ErrorResponse(
                        "Brak danych dla określonego zakresu czasowego",
                        "NOT_FOUND",
                        LocalDateTime.now().toString()
                );
                status = HttpStatus.NOT_FOUND;
            } else if (httpEx.getStatusCode() == HttpStatus.BAD_REQUEST) {
                errorResponse = new ErrorResponse(
                        "Nieprawidłowo sformułowane zapytanie lub przekroczony limit danych",
                        "BAD_REQUEST",
                        LocalDateTime.now().toString()
                );
                status = HttpStatus.BAD_REQUEST;
            } else {
                errorResponse = new ErrorResponse(
                        "Błąd API NBP",
                        "API_ERROR",
                        LocalDateTime.now().toString()
                );
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            errorResponse = new ErrorResponse(
                    "Błąd API NBP",
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
