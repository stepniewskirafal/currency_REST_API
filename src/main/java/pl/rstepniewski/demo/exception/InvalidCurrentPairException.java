package pl.rstepniewski.demo.exception;

import java.io.Serial;

public class InvalidCurrentPairException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidCurrentPairException(String message) {
        super(message);
    }
}
