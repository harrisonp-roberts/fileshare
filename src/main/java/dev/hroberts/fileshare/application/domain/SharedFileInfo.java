package dev.hroberts.fileshare.application.domain;

import java.util.UUID;

public class SharedFileInfo {
    public String filePath;
    public String originalFile;
    public String fileName;
    public int downloadLimit;
    public int remainingDownloads;
    public UUID fileId;
    public String downloadUrl;
}
