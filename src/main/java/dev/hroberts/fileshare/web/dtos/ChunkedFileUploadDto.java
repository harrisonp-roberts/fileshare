package dev.hroberts.fileshare.web.dtos;

import java.util.UUID;

public class ChunkedFileUploadDto {
    public UUID id;
    public String name;
    public long size;
    public int downloadLimit;
}
