package dev.hroberts.fileshare.fileupload.domain.exceptions;

public class FailedToSaveChunkException extends IDomainException {
    public FailedToSaveChunkException() {
        super("An error occured saving the chunk");
    }
}
