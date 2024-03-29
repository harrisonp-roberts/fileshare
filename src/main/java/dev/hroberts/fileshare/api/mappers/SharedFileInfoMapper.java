package dev.hroberts.fileshare.api.mappers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;

public class SharedFileInfoMapper {
    public static SharedFileInfoDto MapDomainToDto(SharedFileInfo sharedFileInfo) {
        var sharedFileInfoDto = new SharedFileInfoDto();
        sharedFileInfoDto.fileId = sharedFileInfo.fileId;
        sharedFileInfoDto.fileName = sharedFileInfo.fileName;
        sharedFileInfoDto.downloadLimit = sharedFileInfo.downloadLimit;
        return sharedFileInfoDto;
    }
}
