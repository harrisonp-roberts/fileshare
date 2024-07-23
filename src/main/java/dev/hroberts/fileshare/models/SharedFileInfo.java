package dev.hroberts.fileshare.models;

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
    public long fileSize;
    public boolean ready;

    public SharedFileInfo() {
        id = UUID.randomUUID();
    }

    public SharedFileInfo(UUID id, String fileName, long fileSize, int downloadLimit) {
        this.id = id;
        this.fileName = fileName;
        this.downloadLimit = downloadLimit;
        this.fileSize = fileSize;
        remainingDownloads = downloadLimit;
        ready = false;
    }

    public void download() {
        if(downloadLimit > 0) {
            remainingDownloads--;
        }
    }
}
