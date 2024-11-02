package pl.rstepniewski.demo.exception;

public class InvalidCurrentPairException extends RuntimeException{

    public InvalidCurrentPairException(String message) {
        super(message);
    }
}
