package dev.hroberts.fileshare.models.exceptions;

public class ChunkAlreadyExistsException extends DomainException {
    public ChunkAlreadyExistsException() {
        super("Failed to save multiple chunks with the same index");
    }
}
