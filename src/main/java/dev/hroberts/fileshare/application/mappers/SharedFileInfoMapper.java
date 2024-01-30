package dev.hroberts.fileshare.application.mappers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;

import java.net.URI;

public class SharedFileInfoMapper {

    public static SharedFileInfoDto MapDomainToDto(SharedFileInfo sharedFileInfo) {
        var sharedFileInfoDto = new SharedFileInfoDto();
        sharedFileInfoDto.fileId = sharedFileInfo.fileId;
        sharedFileInfoDto.filePath = sharedFileInfo.filePath;
        sharedFileInfoDto.downloadLimit = sharedFileInfo.downloadLimit;
        sharedFileInfoDto.downloadUrl = sharedFileInfo.downloadUrl;

        return sharedFileInfoDto;
    }

    public static SharedFileInfo MapDtoToDomain(SharedFileInfoDto sharedFileInfoDto) {
        //todo clean this up
        var fileUri = URI.create(sharedFileInfoDto.filePath);
        var filePath = fileUri.getPath();
        var fileName = filePath.substring(filePath.lastIndexOf('/') + 1);

        var sharedFileInfo = new SharedFileInfo();
        sharedFileInfo.fileId = sharedFileInfoDto.fileId;
        sharedFileInfo.fileName = fileName;
        sharedFileInfo.filePath = sharedFileInfoDto.filePath;
        sharedFileInfo.downloadLimit = sharedFileInfoDto.downloadLimit;
        sharedFileInfo.downloadUrl = sharedFileInfoDto.downloadUrl;
        return sharedFileInfo;
    }

}
