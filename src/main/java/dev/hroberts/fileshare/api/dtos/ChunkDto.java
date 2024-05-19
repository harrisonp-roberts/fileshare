package dev.hroberts.fileshare.api.dtos;

import org.springframework.web.multipart.MultipartFile;

public class ChunkDto {
    public MultipartFile file;
    public int position;
    public long size;
}
