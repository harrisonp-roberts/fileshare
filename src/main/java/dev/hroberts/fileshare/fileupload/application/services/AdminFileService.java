package dev.hroberts.fileshare.fileupload.application.services;

import dev.hroberts.fileshare.fileupload.controllers.dtos.UploadFileByPathDto;
import dev.hroberts.fileshare.fileupload.domain.FileInfo;
import dev.hroberts.fileshare.fileupload.application.repositories.IFileInfoRepository;
import dev.hroberts.fileshare.fileupload.application.repositories.IFileSystemRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AdminFileService {
    final IFileInfoRepository fileInfoRepository;
    final IFileSystemRepository fileStore;

    public AdminFileService(IFileInfoRepository repository, IFileSystemRepository fileStore) {
        this.fileInfoRepository = repository;
        this.fileStore = fileStore;
    }

    @Deprecated
    public FileInfo uploadByPath(UploadFileByPathDto uploadFileByPathDto) throws IOException {
        var fileName = extractFileName(uploadFileByPathDto.filePath);
        var sharedFileInfo = new FileInfo(UUID.randomUUID(),fileName, LocalDateTime.now(ZoneId.of("UTC")));

        fileStore.copyFileIn(sharedFileInfo.id, uploadFileByPathDto.filePath, sharedFileInfo.fileName);
        return fileInfoRepository.save(sharedFileInfo);
    }

    public List<FileInfo> listFiles() {
        var fileInfoIterable = fileInfoRepository.findAll();
        return StreamSupport.stream(fileInfoIterable.spliterator(), false).collect(Collectors.toList());
    }

    public void purgeFiles() {
        var files = fileInfoRepository.findAll();
        files.forEach(fileInfo -> {
            fileStore.deleteFileByName(fileInfo.id, fileInfo.fileName);
            fileInfoRepository.delete(fileInfo);
        });
    }

    private String extractFileName(String filePath) {
        var fileUri = URI.create(filePath);
        var path = fileUri.getPath();
        return path.substring(filePath.lastIndexOf('/') + 1);
    }
}
