package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserFileService {

    final FileInfoRepository fileInfoRepository;

    final FileService fileService;

    public UserFileService(FileInfoRepository fileInfoRepository, FileService fileService) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileService = fileService;
    }

    //todo create an IUserFileService that returns a Resource maybe?
    public PathResource downloadFileById(UUID fileId) {
        var fileInfo = fileInfoRepository.getFileById(fileId);
        var filePath = fileInfo.filePath;
        var tempFilePath = "/home/hroberts/.cache/fileshare/" + fileInfo.fileName;
        fileService.copyFile(filePath, tempFilePath);
        var tempPath = Path.of(tempFilePath);
        return new PathResource(tempPath);
    }

    public Path getPath(UUID fileId) {
        var fileInfo = fileInfoRepository.getFileById(fileId);
        var filePath = fileInfo.filePath;
        return fileService.getPath(filePath);
    }
}
