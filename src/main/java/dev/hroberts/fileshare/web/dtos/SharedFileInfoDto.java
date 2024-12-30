package dev.hroberts.fileshare.web.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class SharedFileInfoDto {
    public String fileName;
    public UUID id;
    public String fileSize;
    public int downloadLimit;
    public int remainingDownloads;
    public boolean ready;
    public long uploadStart;
    public long uploadEnd;
}
