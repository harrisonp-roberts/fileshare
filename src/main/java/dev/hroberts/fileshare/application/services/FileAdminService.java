package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.application.services.FileService;
import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FileAdminService {
    /* TODO:
     * - Will have connections to two file systems. One for intaking/(staging?), the other for persisting. This will prevent someone
     * from uploading a file, then changing it in place. Once it's uploaded, that version should stay the same no matter what happens to the original
     *
     * - First, read file from file system
     * - Then, save that file to the persistence file system
     * - Then, save the file info
     */
    final FileInfoRepository fileInfoRepository;

    final FileService fileService;

    public FileAdminService(FileInfoRepository fileInfoRepository, FileService fileService) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileService = fileService;
    }

    public SharedFileInfoDto uploadFileByPath(SharedFileInfoDto sharedFileInfoDto) {
        var newFileId = UUID.randomUUID();
        var outputFilePath = "/home/hroberts/files/" + newFileId;
        fileService.copyFile(sharedFileInfoDto.filePath, outputFilePath);

        //todo validate the dto and stuff
        //todo also assign the file path in a less gross way
        var sharedFileInfo = SharedFileInfoMapper.MapDtoToDomain(sharedFileInfoDto);
        sharedFileInfo.filePath = outputFilePath;
        sharedFileInfo.fileId = newFileId;
        sharedFileInfo.remainingDownloads = sharedFileInfo.downloadLimit;

        var responseSharedFileInfo = fileInfoRepository.saveFileInfo(sharedFileInfo);
        return SharedFileInfoMapper.MapDomainToDto(responseSharedFileInfo);
    }

    public List<SharedFileInfoDto> listAllFiles() {
        var fileInfoList = fileInfoRepository.listFileInfo();
        return fileInfoList.stream().map(SharedFileInfoMapper::MapDomainToDto).toList();
    }

    public void purgeFiles() {
        var files = fileInfoRepository.listFileInfo();
        files.forEach(sharedFileInfo -> {
            fileService.deleteFile(sharedFileInfo.filePath);
            fileInfoRepository.deleteFileInfo(sharedFileInfo.fileId);
        });
    }
}
