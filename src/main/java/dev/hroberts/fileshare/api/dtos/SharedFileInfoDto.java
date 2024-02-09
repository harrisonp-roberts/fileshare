package dev.hroberts.fileshare.api.dtos;

import java.util.UUID;

public class SharedFileInfoDto {
    public String fileName;
    public int downloadLimit;
    public UUID fileId;
    public String downloadUrl;
}
