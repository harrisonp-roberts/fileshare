package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.api.dtos.UploadFileByPathDto;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.application.repositories.FileInfoRepository;
import dev.hroberts.fileshare.persistence.filestore.IFileStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AdminFileService {
    final FileInfoRepository fileInfoRepository;
    final IFileStore fileStore;

    public AdminFileService(FileInfoRepository repository, IFileStore fileStore) {
        this.fileInfoRepository = repository;
        this.fileStore = fileStore;
    }

    @Deprecated
    public SharedFileInfo uploadByPath(UploadFileByPathDto uploadFileByPathDto) throws IOException {
        var fileName = extractFileName(uploadFileByPathDto.filePath);
        var sharedFileInfo = new SharedFileInfo(fileName, uploadFileByPathDto.downloadLimit);

        fileStore.copyFileIn(uploadFileByPathDto.filePath, sharedFileInfo.fileId);
        return fileInfoRepository.save(sharedFileInfo);
    }

    public List<SharedFileInfo> listFiles() {
        var fileInfoIterable = fileInfoRepository.findAll();
        return StreamSupport.stream(fileInfoIterable.spliterator(), false).collect(Collectors.toList());
    }

    public void purgeFiles() {
        var files = fileInfoRepository.findAll();
        files.forEach(sharedFileInfo -> {
            fileStore.deleteFileByName(sharedFileInfo.fileId);
            fileInfoRepository.delete(sharedFileInfo);
        });
    }

    private String extractFileName(String filePath) {
        var fileUri = URI.create(filePath);
        var path = fileUri.getPath();
        return path.substring(filePath.lastIndexOf('/') + 1);
    }
}
