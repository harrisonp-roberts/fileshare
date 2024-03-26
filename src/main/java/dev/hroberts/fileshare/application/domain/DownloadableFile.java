package dev.hroberts.fileshare.application.domain;

import java.nio.file.Path;

public class DownloadableFile {
    public String fileName;
    public Path filePath;

    public DownloadableFile(String fileName, Path filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
