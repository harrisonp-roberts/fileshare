package dev.hroberts.fileshare.fileupload.domain.exceptions;

public class InvalidFileNameException extends IDomainException {
    public InvalidFileNameException() {
        super("Invalid file name");
    }
}
