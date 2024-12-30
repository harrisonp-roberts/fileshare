package dev.hroberts.fileshare.web.dtos.mappers;

import dev.hroberts.fileshare.web.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.models.SharedFileInfo;

import java.text.DecimalFormat;
import java.time.ZoneOffset;

public class SharedFileInfoMapper {
    public static SharedFileInfoDto mapToDto(SharedFileInfo sharedFileInfo) {
        var sharedFileInfoDto = new SharedFileInfoDto();
        sharedFileInfoDto.id = sharedFileInfo.id;
        sharedFileInfoDto.fileName = sharedFileInfo.fileName;
        sharedFileInfoDto.fileSize = prettyPrintFileSize(sharedFileInfo.fileSize);
        sharedFileInfoDto.remainingDownloads = sharedFileInfo.remainingDownloads;
        sharedFileInfoDto.downloadLimit = sharedFileInfo.downloadLimit;
        sharedFileInfoDto.ready = sharedFileInfo.ready;
        sharedFileInfoDto.uploadStart = sharedFileInfo.uploadStart.toInstant(ZoneOffset.UTC).toEpochMilli();
        sharedFileInfoDto.uploadEnd = sharedFileInfo.uploadEnd.toInstant(ZoneOffset.UTC).toEpochMilli();
        return sharedFileInfoDto;
    }

    private static String prettyPrintFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "PB", "EB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
