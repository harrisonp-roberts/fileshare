package dev.hroberts.fileshare.fileupload.application.services.exceptions;

public class InvalidHashException extends Exception {
    public InvalidHashException(String message) {
        super(message);
    }
}
