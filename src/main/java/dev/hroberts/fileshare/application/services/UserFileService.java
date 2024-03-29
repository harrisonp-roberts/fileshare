package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.application.domain.DownloadableFile;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.application.repositories.FileInfoRepository;
import dev.hroberts.fileshare.persistence.filestore.IFileStore;
import org.apache.commons.fileupload2.core.FileItemInputIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class UserFileService {
    final String host;
    final FileInfoRepository fileInfoRepository;
    final IFileStore localFileStore;

    public UserFileService(FileInfoRepository fileInfoRepository, IFileStore localFileStore, @Value("${host}") String host) {
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

    public SharedFileInfo uploadFile(FileItemInputIterator fileInputIterator) throws IOException {
        var fileInfo = new SharedFileInfo();

        try {
            fileInputIterator.forEachRemaining(item -> {
                InputStream stream = item.getInputStream();
                if (item.isFormField()) {
                    var fieldValue = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    parseField(fileInfo, item.getFieldName(), fieldValue);
                } else {
                    fileInfo.fileName = item.getName();
                    localFileStore.save(stream, fileInfo.fileId);
                }
            });
        } catch (IOException ex) {
            localFileStore.deleteFileByName(fileInfo.fileName);
            throw ex;
        }

        fileInfoRepository.save(fileInfo);
        return fileInfo;
    }

    private void parseField(SharedFileInfo fileInfo, String fieldName, String fieldValue) {
        if (fieldName.equals("downloadLimit")) {
            fileInfo.downloadLimit = Integer.parseInt(fieldValue);
        }
    }
}