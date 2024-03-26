package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.domain.DownloadableFile;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.api.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.application.repositories.FileInfoRepository;
import dev.hroberts.fileshare.persistence.filestore.IFileStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class UserFileService {
    final String host;
    final FileInfoRepository fileInfoRepository;
    final IFileStore localFileStore;

    public UserFileService(FileInfoRepository fileInfoRepository, IFileStore localFileStore,
                           @Value("${host}") String host) {
        this.fileInfoRepository = fileInfoRepository;
        this.localFileStore = localFileStore;
        this.host = host;
    }
    
    public DownloadableFile downloadFile(String fileId) throws FileNotFoundException {
        var fileInfo = fileInfoRepository.findById(fileId).orElseThrow();
        fileInfo.download();
        var filePath = localFileStore.load(fileInfo.fileId);
        var fileName = fileInfo.fileName;
        fileInfoRepository.save(fileInfo);
        return new DownloadableFile(fileName, filePath);
    }

    public SharedFileInfo uploadFile(MultipartFile file, int downloadLimit) {
        var fileInfo = new SharedFileInfo(file.getOriginalFilename(), downloadLimit);
        fileInfoRepository.save(fileInfo);

        try {
            localFileStore.save(file.getInputStream(), fileInfo.fileId);
        } catch (IOException e) {
            fileInfoRepository.delete(fileInfo);
            throw new RuntimeException("Could not upload file");
        }

        return fileInfo;
    }
}