package dev.hroberts.fileshare.application;

import dev.hroberts.fileshare.api.dto.SharedFileInfoDto;
import dev.hroberts.fileshare.application.Mapper.SharedFileInfoMapper;
import dev.hroberts.fileshare.application.services.FileService;
import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    FileService fileService;

    public SharedFileInfoDto uploadFileByPath(SharedFileInfoDto fileInfoDto) {
        var newFileId = UUID.randomUUID();
        var outputFilePath = "/home/hroberts/files/" + newFileId;
        fileService.copyFile(fileInfoDto.filePath, outputFilePath);

        //todo validate the dto and stuff
        //todo also assign the file path in a less gross way
        var sharedFileInfo = SharedFileInfoMapper.MapDtoToDomain(fileInfoDto);
        sharedFileInfo.filePath = outputFilePath;
        sharedFileInfo.fileId = newFileId;

        var responseSharedFileInfo = fileInfoRepository.saveFileInfo(sharedFileInfo);
        return SharedFileInfoMapper.MapDomainToDto(responseSharedFileInfo);
    }
}
