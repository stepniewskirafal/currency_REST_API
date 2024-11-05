package pl.rstepniewski.demo.exception;

import java.io.Serial;

public class NegativeInitialBalanceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public NegativeInitialBalanceException(String message) {
        super(message);
    }
}
