package dev.hroberts.fileshare.fileupload.domain.domainexceptions;

public class InvalidFileNameException extends IDomainException {
    public InvalidFileNameException() {
        super("Invalid file name");
    }
}
