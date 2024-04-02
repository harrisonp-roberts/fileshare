package dev.hroberts.fileshare.api.dtos;

public class InitiateChunkedFileUploadRequest {
    public String name;
    public long size;
    public int downloadLimit;
}
