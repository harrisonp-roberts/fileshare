package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.api.dtos.UploadFileByPathDto;
import dev.hroberts.fileshare.application.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.persistence.filestore.LocalFileStore;
import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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

    final LocalFileStore fileStore;

    //todo inject the interface instead
    public FileAdminService(FileInfoRepository fileInfoRepository, LocalFileStore fileStore) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileStore = fileStore;
    }

    public SharedFileInfoDto uploadFileByPath(UploadFileByPathDto uploadFileByPathDto) throws IOException {
        var sharedFileInfo = SharedFileInfoMapper.MapUploadByPathDtoToDomain(uploadFileByPathDto);

        fileStore.copyFileIn(uploadFileByPathDto.filePath, sharedFileInfo.fileId.toString());
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
            fileStore.deleteFileByName(sharedFileInfo.fileId.toString());
            fileInfoRepository.deleteFileInfo(sharedFileInfo.fileId);
        });
    }
}
