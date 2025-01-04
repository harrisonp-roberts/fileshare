package dev.hroberts.fileshare.fileupload.application.services.exceptions;

public class InvalidHashAlgorithmException extends Exception {
    public InvalidHashAlgorithmException(String message) {
        super(message);
    }
}
