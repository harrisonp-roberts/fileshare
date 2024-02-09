package dev.hroberts.fileshare.application.mappers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.api.dtos.UploadFileByPathDto;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;

import java.net.URI;
import java.util.UUID;

public class SharedFileInfoMapper {

    public static SharedFileInfoDto MapDomainToDto(SharedFileInfo sharedFileInfo) {
        var sharedFileInfoDto = new SharedFileInfoDto();
        sharedFileInfoDto.fileId = sharedFileInfo.fileId;
        sharedFileInfoDto.fileName = sharedFileInfo.fileName;
        sharedFileInfoDto.downloadLimit = sharedFileInfo.downloadLimit;
        sharedFileInfoDto.downloadUrl = sharedFileInfo.downloadUrl;
        return sharedFileInfoDto;
    }

    //todo the below should NOT be here move it to a factory or to a Create static method
    public static SharedFileInfo MapUploadByPathDtoToDomain(UploadFileByPathDto uploadFileByPathDto) {
        var fileName = extractFileName(uploadFileByPathDto.filePath);
        var sharedFileInfo = new SharedFileInfo();
        sharedFileInfo.fileId = UUID.randomUUID();
        sharedFileInfo.fileName = fileName;
        sharedFileInfo.downloadLimit = 10;
        return sharedFileInfo;
    }

    private static String extractFileName(String filePath) {
        var fileUri = URI.create(filePath);
        var path = fileUri.getPath();
        return path.substring(filePath.lastIndexOf('/') + 1);
    }
}
