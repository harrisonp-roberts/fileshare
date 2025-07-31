package dev.hroberts.fileshare.fileupload.controllers.dtos.mappers;

import dev.hroberts.fileshare.fileupload.controllers.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.fileupload.domain.FileInfo;

import java.text.DecimalFormat;
import java.time.ZoneOffset;

public class SharedFileInfoMapper {
    public static SharedFileInfoDto mapToDto(FileInfo fileInfo) {
        var sharedFileInfoDto = new SharedFileInfoDto();
        sharedFileInfoDto.id = fileInfo.id;
        sharedFileInfoDto.fileName = fileInfo.fileName;
        sharedFileInfoDto.fileSize = prettyPrintFileSize(fileInfo.fileSize);
        sharedFileInfoDto.ready = fileInfo.ready;
        sharedFileInfoDto.uploadStart = fileInfo.uploadStart.toInstant(ZoneOffset.UTC).toEpochMilli();
        sharedFileInfoDto.uploadEnd = fileInfo.uploadEnd.toInstant(ZoneOffset.UTC).toEpochMilli();
        return sharedFileInfoDto;
    }

    private static String prettyPrintFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "PB", "EB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
