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

    public UserFileService(FileInfoRepository fileInfoRepository,
                           IFileStore localFileStore, @Value("${host}") String host) {
        this.fileInfoRepository = fileInfoRepository;
        this.localFileStore = localFileStore;
        this.host = host;
    }

    public PathResource downloadFile(String )

    public PathResource downloadFileById(UUID fileId) throws IOException {
        var fileInfo = fileInfoRepository.getFileById(fileId);
        var cachedFile = localFileStore.cacheFile(fileId.toString(), fileInfo.fileName);
        return new PathResource(cachedFile);
    }

    public SharedFileInfoDto storeFile(MultipartFile file, int downloadLimit) {
        var fileId = UUID.randomUUID();
        var fileInfo = new SharedFileInfo();

        try {
            localFileStore.storeFile(file.getInputStream(), fileId.toString());
        } catch (IOException e) {
            throw new RuntimeException("Could not upload file");
        }

        fileInfo.fileName = file.getOriginalFilename();
        fileInfo.downloadLimit = downloadLimit;
        fileInfo.fileId = fileId;
        fileInfo.downloadUrl = generateDownloadUrl();
        fileInfoRepository.saveFileInfo(fileInfo);
        return SharedFileInfoMapper.MapDomainToDto(fileInfo);
    }

    private String generateDownloadUrl() {
        var shortId = RandomStringUtils.randomAlphabetic(8);
        return host + "/download/" + shortId;
    }
}