package dev.hroberts.fileshare.fileupload.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RedisHash("FileInfo")
public class FileInfo {
    public String fileName;

    @Id
    public UUID id;
    public long fileSize;
    public boolean ready;
    public LocalDateTime uploadStart;
    public LocalDateTime uploadEnd;

    public FileInfo() {
        id = UUID.randomUUID();
    }

    public FileInfo(UUID id, String fileName, LocalDateTime uploadStart) {
        this.id = id;
        this.fileName = fileName;
        this.uploadStart = uploadStart;
        this.uploadEnd = LocalDateTime.now(ZoneId.of("UTC"));
        ready = false;
    }

    public FileInfo(String fileName, LocalDateTime uploadStart) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.uploadStart = uploadStart;
        this.uploadEnd = LocalDateTime.now(ZoneId.of("UTC"));
        ready = false;
    }
}
