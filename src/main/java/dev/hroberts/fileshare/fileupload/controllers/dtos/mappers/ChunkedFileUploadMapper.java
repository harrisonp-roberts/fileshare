package dev.hroberts.fileshare.fileupload.controllers.dtos.mappers;

import dev.hroberts.fileshare.fileupload.controllers.dtos.ChunkedFileUploadDto;
import dev.hroberts.fileshare.fileupload.domain.ChunkedFileUpload;

import java.time.ZoneOffset;

public class ChunkedFileUploadMapper {
    public static ChunkedFileUploadDto mapToDto     (ChunkedFileUpload chunkedFileUpload) {
        var chunkedFileUploadDto = new ChunkedFileUploadDto();
        chunkedFileUploadDto.id = chunkedFileUpload.id;
        chunkedFileUploadDto.name = chunkedFileUpload.name;
        chunkedFileUploadDto.startTime = chunkedFileUpload.startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        return chunkedFileUploadDto;
    }
}
