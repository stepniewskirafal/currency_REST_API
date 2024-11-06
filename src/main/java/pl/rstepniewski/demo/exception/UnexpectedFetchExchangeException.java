package pl.rstepniewski.demo.exception;

import java.io.Serial;

public class UnexpectedFetchExchangeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public UnexpectedFetchExchangeException(String message) {
        super(message);
    }
}
