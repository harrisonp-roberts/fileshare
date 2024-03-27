package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.domain.DownloadableFile;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.api.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.application.repositories.FileInfoRepository;
import dev.hroberts.fileshare.persistence.filestore.IFileStore;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileItemInputIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

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

    public SharedFileInfo uploadFile(FileItemInputIterator fileInputIterator) {
        var fields = new HashMap<String, String>();
        var fileInfo = new SharedFileInfo();
        //todo this needs a mapper or something cause this approach ain't it

        try {
            fileInputIterator.forEachRemaining(item -> {
                InputStream stream = item.getInputStream();
                if (item.isFormField()) {
                    fields.put(item.getFieldName(), Arrays.toString(stream.readAllBytes()));
                } else {
                    fields.put("fileName", item.getName());
                    localFileStore.save(stream, fileInfo.fileId);
                }
            });

            parseFormFields(fields, fileInfo);
            fileInfoRepository.save(fileInfo);
            return fileInfo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseFormFields(HashMap<String, String> fields, SharedFileInfo fileInfo) {
        //todo bad
        var fileName = Optional.ofNullable(fields.get("fileName")).orElseThrow(() -> new RuntimeException("Filename is required"));
        fileInfo.downloadLimit = 1;
        fileInfo.fileName = fileName;
    }
}