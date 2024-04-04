package dev.hroberts.fileshare.api.dtos;

public class InitiateChunkedFileUploadRequestDto {
    public String name;
    public long size;
    public int downloadLimit;
}
