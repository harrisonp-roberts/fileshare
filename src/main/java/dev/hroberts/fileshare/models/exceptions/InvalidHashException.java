package dev.hroberts.fileshare.models.exceptions;

public class InvalidHashException extends DomainException {
    InvalidHashException() {
        super("Hash mismatch");
    }
}
