package dev.hroberts.fileshare.models.exceptions;

public class InvalidFileNameException extends DomainException {
    public InvalidFileNameException() {
        super("Invalid file name");
    }
}
