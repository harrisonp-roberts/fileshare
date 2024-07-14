package dev.hroberts.fileshare.api.mappers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;

public class SharedFileInfoMapper {
    public static SharedFileInfoDto MapDomainToDto(SharedFileInfo sharedFileInfo) {
        var sharedFileInfoDto = new SharedFileInfoDto();
        sharedFileInfoDto.id = sharedFileInfo.id;
        sharedFileInfoDto.fileName = sharedFileInfo.fileName;
        sharedFileInfoDto.fileSize = sharedFileInfo.fileSize;
        sharedFileInfoDto.downloadLimit = sharedFileInfo.downloadLimit;
        sharedFileInfoDto.ready = sharedFileInfo.ready;
        return sharedFileInfoDto;
    }
}
