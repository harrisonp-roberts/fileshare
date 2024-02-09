package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.application.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.persistence.filestore.LocalFileStore;
import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserFileService {

    final FileInfoRepository fileInfoRepository;
    final LocalFileStore localFileStore;

    public UserFileService(FileInfoRepository fileInfoRepository, LocalFileStore localFileStore) {
        this.fileInfoRepository = fileInfoRepository;
        this.localFileStore = localFileStore;
    }

    //todo create an IUserFileService that returns a Resource maybe?
    public PathResource downloadFileById(UUID fileId) throws IOException {
        var fileInfo = fileInfoRepository.getFileById(fileId);
        var cachedFile = localFileStore.cacheFile(fileId.toString(), fileInfo.fileName);
        ;
        return new PathResource(cachedFile);
    }

    //todo refactor all references maxUploads/downloadLimits to have same name
    public SharedFileInfoDto storeFile(MultipartFile file, int maxUploads) throws IOException {
        var fileId = UUID.randomUUID();
        var fileInfo = new SharedFileInfo();
        localFileStore.storeFile(file.getInputStream(), fileId.toString());
        fileInfo.fileName = file.getOriginalFilename();
        fileInfo.downloadLimit = maxUploads;
        fileInfo.fileId = fileId;
        fileInfoRepository.saveFileInfo(fileInfo);
        return SharedFileInfoMapper.MapDomainToDto(fileInfo);
    }
}