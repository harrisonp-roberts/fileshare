package dev.hroberts.fileshare.api.controllers;

import dev.hroberts.fileshare.api.dtos.InitiateChunkedFileUploadRequestDto;
import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.api.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.application.exceptions.ChunkAlreadyExistsException;
import dev.hroberts.fileshare.application.exceptions.ChunkSizeOutOfBoundsException;
import dev.hroberts.fileshare.application.exceptions.ChunkedUploadCompletedException;
import dev.hroberts.fileshare.application.services.UserFileService;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("files")
public class UserFileController {
    private final UserFileService userFileService;

    public UserFileController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    //todo allow various metadata to be associated with the file
    @PostMapping("/initiate-multipart")
    public @ResponseBody ResponseEntity<UUID> initiateMultipartUpload(@RequestBody InitiateChunkedFileUploadRequestDto request) {
        var uploadId = userFileService.initiateChunkedUpload(request.name, request.size, request.downloadLimit);
        return ResponseEntity.ok(uploadId);
    }

    @PostMapping("/upload/{uploadId}")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> uploadChunk(@RequestPart MultipartFile file, @PathVariable UUID uploadId, @RequestParam int position, @RequestParam long size) {
        try {
            userFileService.saveChunk(uploadId, size, position, file.getInputStream());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (ChunkAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        } catch (ChunkSizeOutOfBoundsException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/complete/{uploadId}")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> completeUpload(@PathVariable UUID uploadId) {
        try {
            var response = userFileService.completeUpload(uploadId);
            return ResponseEntity.ok(SharedFileInfoMapper.MapDomainToDto(response));
        } catch (ChunkedUploadCompletedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/download/{uploadId}/{fileName}")
    public @ResponseBody ResponseEntity<Resource> getFile(@PathVariable UUID uploadId, @PathVariable String fileId) {
        try {
            var downloadableFile = userFileService.downloadFile(uploadId, fileId);
            var response = new PathResource(downloadableFile.filePath);
            var contentLength = response.contentLength();
            var headers = new HttpHeaders();
            var mediaType = MediaTypeFactory.getMediaType(response).orElse(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentType(mediaType);
            headers.add("Content-Disposition", "attachment; filename=" + downloadableFile.fileName);

            return ResponseEntity.ok().contentLength(contentLength).contentType(MediaType.APPLICATION_OCTET_STREAM).headers(headers).body(response);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
