package dev.hroberts.fileshare.models;

import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import java.nio.file.Path;

public class DownloadableFile {
    public boolean shouldDelete;
    public String fileName;
    public Path filePath;
    public MediaType mediaType;
    public long contentLength;

    public DownloadableFile(String fileName, Path filePath, boolean shouldDelete) {
        this.shouldDelete = shouldDelete;
        this.fileName = fileName;
        this.filePath = filePath;
        this.mediaType = MediaTypeFactory.getMediaType(filePath.toString()).orElse(MediaType.APPLICATION_OCTET_STREAM);
        this.contentLength = filePath.toFile().length();
    }
}
