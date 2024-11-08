package dev.hroberts.fileshare.models.exceptions;

public class FailedToSaveChunkException extends DomainException{
    public FailedToSaveChunkException() {
        super("An error occured saving the chunk");
    }
}
