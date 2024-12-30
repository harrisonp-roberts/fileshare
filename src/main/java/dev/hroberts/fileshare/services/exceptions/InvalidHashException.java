package dev.hroberts.fileshare.services.exceptions;

public class InvalidHashException extends Exception {
    public InvalidHashException(String message) {
        super(message);
    }
}
