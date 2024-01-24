package dev.hroberts.fileshare.api.dtos;

import java.util.UUID;

//todo maybe I'll need an "UploadSharedFileInfoDto" at some point.
public class SharedFileInfoDto {
    public String filePath;
    public String fileName;
    public int downloadLimit;
    public UUID fileId;
    public String downloadUrl;
}
