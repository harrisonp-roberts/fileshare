package dev.hroberts.fileshare.application.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("SharedFileInfo")
public class SharedFileInfo {
    @Id
    public String fileId;
    public String fileName;
    public int downloadLimit;
    public int remainingDownloads;
}
