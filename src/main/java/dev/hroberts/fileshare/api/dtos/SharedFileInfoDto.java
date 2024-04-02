package dev.hroberts.fileshare.api.dtos;

import java.util.UUID;

public class SharedFileInfoDto {
    public String fileName;
    public UUID uploadId;
    public String fileId;
    public int downloadLimit;
}
