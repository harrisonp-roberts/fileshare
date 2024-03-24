package dev.hroberts.fileshare.application.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("SharedFileInfo")
public class SharedFileInfo {
    @Id
    public String fileId;
    public String fileName;
    public int downloadLimit;
    public int remainingDownloads;

    public SharedFileInfo() {
    }

    public SharedFileInfo(String fileName, int downloadLimit) {
        fileId = RandomStringUtils.randomAlphabetic(8);
        this.fileName = fileName;
        this.downloadLimit = downloadLimit;
        remainingDownloads = downloadLimit;
    }
}
