package dev.hroberts.fileshare.api.dtos;

import java.util.UUID;

public class SharedFileInfoDto {
    public String fileName;
    public UUID id;
    public long fileSize;
    public int downloadLimit;
    public boolean ready;
}
