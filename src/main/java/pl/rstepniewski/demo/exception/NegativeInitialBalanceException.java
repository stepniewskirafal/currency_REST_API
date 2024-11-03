package pl.rstepniewski.demo.exception;

public class NegativeInitialBalanceException extends RuntimeException {

    public NegativeInitialBalanceException(String message) {
        super(message);
    }
}
