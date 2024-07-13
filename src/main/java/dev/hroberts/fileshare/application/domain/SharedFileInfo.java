package dev.hroberts.fileshare.application.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash("SharedFileInfo")
public class SharedFileInfo {
    public String fileName;

    @Id
    public UUID id;
    public int downloadLimit;
    public int remainingDownloads;
    public boolean ready;

    public SharedFileInfo() {
        id = UUID.randomUUID();
    }

    public SharedFileInfo(UUID id, String fileName, int downloadLimit) {
        this.id = id;
        this.fileName = fileName;
        this.downloadLimit = downloadLimit;
        remainingDownloads = downloadLimit;
        ready = false;
    }

    public void download() {
        remainingDownloads--;
    }
}
