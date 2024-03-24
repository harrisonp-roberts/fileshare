package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.application.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.persistence.filestore.IFileStore;
import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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
    
    public PathResource downloadFileById(String fileId) throws IOException {
        //todo would like to get rid of the whole cache thing.
        var fileInfo = fileInfoRepository.getFileById(fileId);
        var cachedFile = localFileStore.cacheFile(fileId, fileInfo.fileName);
        return new PathResource(cachedFile);
    }

    public SharedFileInfoDto storeFile(MultipartFile file, int downloadLimit) {
        var fileInfo = new SharedFileInfo(file.getOriginalFilename(), downloadLimit);
        fileInfoRepository.saveFileInfo(fileInfo);

        try {
            localFileStore.storeFile(file.getInputStream(), fileInfo.fileId);
        } catch (IOException e) {
            fileInfoRepository.deleteFileInfo(fileInfo.fileId);
            throw new RuntimeException("Could not upload file");
        }

        return SharedFileInfoMapper.MapDomainToDto(fileInfo);
    }
}