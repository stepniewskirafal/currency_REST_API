package pl.rstepniewski.demo.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String errorCode;
    private String timestamp;
}
