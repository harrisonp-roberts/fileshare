package dev.hroberts.fileshare.fileupload.application.services.exceptions;

public class UploadAlreadyCompletedException extends Exception {
    public UploadAlreadyCompletedException(String message) {
        super(message);
    }
}
