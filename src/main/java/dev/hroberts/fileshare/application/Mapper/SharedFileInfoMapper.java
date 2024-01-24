package dev.hroberts.fileshare.application.Mapper;

import dev.hroberts.fileshare.api.dto.SharedFileInfoDto;
import dev.hroberts.fileshare.application.domain.SharedFile;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;

public class SharedFileInfoMapper {

    public static SharedFileInfoDto MapDomainToDto(SharedFileInfo sharedFileInfo) {
        var fileInfoDto = new SharedFileInfoDto();
        fileInfoDto.fileId = sharedFileInfo.fileId;
        fileInfoDto.fileName = sharedFileInfo.fileName;
        fileInfoDto.filePath = sharedFileInfo.filePath;
        fileInfoDto.downloadUrl = sharedFileInfo.downloadUrl;

        return fileInfoDto;
    }

    public static SharedFileInfo MapDtoToDomain(SharedFileInfoDto sharedFileInfoDto) {
        var sharedFileInfo = new SharedFileInfo();
        sharedFileInfo.fileId = sharedFileInfoDto.fileId;
        sharedFileInfo.fileName = sharedFileInfoDto.fileName;
        sharedFileInfo.filePath = sharedFileInfoDto.filePath;
        sharedFileInfo.downloadUrl = sharedFileInfoDto.downloadUrl;
        return sharedFileInfo;
    }

}
