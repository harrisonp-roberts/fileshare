package dev.hroberts.fileshare.models;

import dev.hroberts.fileshare.models.exceptions.*;
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
    public int downloadLimit;
    public boolean completing = false;
    public LocalDateTime startTime;

    public ChunkedFileUpload(String name, int downloadLimit) throws DomainException {
        if(name == null || name.isEmpty()) throw new InvalidFileNameException();
        id = UUID.randomUUID();
        this.name = name;

        startTime = LocalDateTime.now(ZoneId.of("UTC"));
        if(downloadLimit < 1) {
            this.downloadLimit = -1;
        } else {
            this.downloadLimit = downloadLimit;
        }
    }
}
