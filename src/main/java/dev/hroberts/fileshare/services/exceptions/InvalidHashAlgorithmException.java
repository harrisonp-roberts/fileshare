package dev.hroberts.fileshare.services.exceptions;

public class InvalidHashAlgorithmException extends Exception {
    public InvalidHashAlgorithmException(String message) {
        super(message);
    }
}
