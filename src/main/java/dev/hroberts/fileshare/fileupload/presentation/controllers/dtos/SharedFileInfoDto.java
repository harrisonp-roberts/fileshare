package dev.hroberts.fileshare.fileupload.presentation.controllers.dtos;

import java.util.UUID;

public class SharedFileInfoDto {
    public String fileName;
    public UUID id;
    public String fileSize;
    public boolean ready;
    public long uploadStart;
    public long uploadEnd;
}
