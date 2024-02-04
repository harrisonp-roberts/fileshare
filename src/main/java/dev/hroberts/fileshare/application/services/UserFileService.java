package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import org.springframework.core.io.PathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class UserFileService {

    final FileInfoRepository fileInfoRepository;

    final FileManipulationService fileManipulationService;

    public UserFileService(FileInfoRepository fileInfoRepository, FileManipulationService fileManipulationService) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileManipulationService = fileManipulationService;
    }

    //todo create an IUserFileService that returns a Resource maybe?
    public PathResource downloadFileById(UUID fileId) throws IOException {
        var fileInfo = fileInfoRepository.getFileById(fileId);
        var filePath = fileInfo.filePath;
        var tempFilePath = "/home/hroberts/.cache/fileshare/" + fileInfo.fileId + "/" + fileInfo.fileName;
        fileManipulationService.copyFile(filePath, tempFilePath);
        var tempPath = Path.of(tempFilePath);
        return new PathResource(tempPath);
    }

    public UUID storeFile(MultipartFile file) {
        var fileId = UUID.randomUUID();
        var filePath = fileManipulationService.storeFile(file, fileId);
        var fileInfo = new SharedFileInfo();
        fileInfo.filePath = filePath;
        fileInfo.fileName = file.getOriginalFilename();
        fileInfo.downloadLimit = 10;
        fileInfo.fileId = fileId;
        fileInfoRepository.saveFileInfo(fileInfo);
        return fileId;
    }
}
