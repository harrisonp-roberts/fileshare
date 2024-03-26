package dev.hroberts.fileshare.api.mappers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.api.dtos.UploadFileByPathDto;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.URI;
import java.util.UUID;

public class SharedFileInfoMapper {
    public static SharedFileInfoDto MapDomainToDto(SharedFileInfo sharedFileInfo) {
        var sharedFileInfoDto = new SharedFileInfoDto();
        sharedFileInfoDto.fileId = sharedFileInfo.fileId;
        sharedFileInfoDto.fileName = sharedFileInfo.fileName;
        sharedFileInfoDto.downloadLimit = sharedFileInfo.downloadLimit;
        return sharedFileInfoDto;
    }
}
