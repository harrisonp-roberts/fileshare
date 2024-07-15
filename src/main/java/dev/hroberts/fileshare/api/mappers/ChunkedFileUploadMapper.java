package dev.hroberts.fileshare.api.mappers;

import dev.hroberts.fileshare.api.dtos.ChunkedFileUploadDto;
import dev.hroberts.fileshare.application.domain.ChunkedFileUpload;

public class ChunkedFileUploadMapper {
    public static ChunkedFileUploadDto mapToDto     (ChunkedFileUpload chunkedFileUpload) {
        var chunkedFileUploadDto = new ChunkedFileUploadDto();
        chunkedFileUploadDto.id = chunkedFileUpload.id;
        chunkedFileUploadDto.name = chunkedFileUpload.name;
        chunkedFileUploadDto.downloadLimit = chunkedFileUpload.downloadLimit;
        chunkedFileUploadDto.size = chunkedFileUpload.size;
        return chunkedFileUploadDto;
    }
}
