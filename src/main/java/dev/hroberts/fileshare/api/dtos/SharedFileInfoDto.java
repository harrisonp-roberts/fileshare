package dev.hroberts.fileshare.api.dtos;

import java.util.UUID;

public class SharedFileInfoDto {
    public String fileName;
    public UUID id;
    public String fileSize;
    public int downloadLimit;
    public int remainingDownloads;
    public boolean ready;
}
