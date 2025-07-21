package it.extrared.stadion.exceptions;

public class TemplatingException extends RuntimeException {

    public TemplatingException(Throwable t) {
        super(t);
    }

    public TemplatingException(String msg) {
        super(msg);
    }
}
