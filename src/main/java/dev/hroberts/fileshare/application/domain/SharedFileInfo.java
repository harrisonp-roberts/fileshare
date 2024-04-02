package dev.hroberts.fileshare.application.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash("SharedFileInfo")
public class SharedFileInfo {
    @Id
    public String fileId;
    public String fileName;
    public UUID uploadId;
    public int downloadLimit;
    public int remainingDownloads;

    public SharedFileInfo() {
        fileId = RandomStringUtils.randomAlphabetic(8);
    }

    public SharedFileInfo(UUID uploadId, String fileName, int downloadLimit) {
        fileId = RandomStringUtils.randomAlphabetic(8);
        this.uploadId = uploadId;
        this.fileName = fileName;
        this.downloadLimit = downloadLimit;
        remainingDownloads = downloadLimit;
    }


    public void download() {
        remainingDownloads--;
    }
}
