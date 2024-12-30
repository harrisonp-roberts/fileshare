package dev.hroberts.fileshare.services.exceptions;

public class UploadAlreadyCompletedException extends Exception {
    public UploadAlreadyCompletedException(String message) {
        super(message);
    }
}
