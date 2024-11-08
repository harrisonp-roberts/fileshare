package dev.hroberts.fileshare.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RedisHash("SharedFileInfo")
public class SharedFileInfo {
    public String fileName;

    @Id
    public UUID id;
    public int downloadLimit;
    public int remainingDownloads;
    public long fileSize;
    public boolean ready;
    public LocalDateTime uploadStart;
    public LocalDateTime uploadEnd;

    public SharedFileInfo() {
        id = UUID.randomUUID();
    }

    public SharedFileInfo(UUID id, String fileName, int downloadLimit, LocalDateTime uploadStart) {
        this.id = id;
        this.fileName = fileName;
        this.downloadLimit = downloadLimit;
        this.uploadStart = uploadStart;
        this.uploadEnd = LocalDateTime.now(ZoneId.of("UTC"));
        remainingDownloads = downloadLimit;
        ready = false;
    }

    public void download() {
        if(downloadLimit > 0) {
            remainingDownloads--;
        }
    }
}
