package dev.hroberts.fileshare.fileupload.domain;

import dev.hroberts.fileshare.fileupload.domain.domainexceptions.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RedisHash("ChunkedFileUpload")
public class ChunkedFileUpload {
    @Id
    public UUID id;
    public String name;
    public boolean completing = false;
    public LocalDateTime startTime;

    public ChunkedFileUpload(String name) throws IDomainException {
        if(name == null || name.isEmpty()) throw new InvalidFileNameException();
        id = UUID.randomUUID();
        this.name = name;

        startTime = LocalDateTime.now(ZoneId.of("UTC"));
    }
}
