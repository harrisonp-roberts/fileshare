package dev.hroberts.fileshare.web.dtos.mappers;

import dev.hroberts.fileshare.web.dtos.ChunkedFileUploadDto;
import dev.hroberts.fileshare.models.ChunkedFileUpload;

public class ChunkedFileUploadMapper {
    public static ChunkedFileUploadDto mapToDto     (ChunkedFileUpload chunkedFileUpload) {
        var chunkedFileUploadDto = new ChunkedFileUploadDto();
        chunkedFileUploadDto.id = chunkedFileUpload.id;
        chunkedFileUploadDto.name = chunkedFileUpload.name;
        chunkedFileUploadDto.downloadLimit = chunkedFileUpload.downloadLimit;
        chunkedFileUploadDto.startTime = chunkedFileUpload.startTime;
        return chunkedFileUploadDto;
    }
}
