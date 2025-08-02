package dev.hroberts.fileshare.fileupload.controllers;

import dev.hroberts.fileshare.fileupload.application.services.exceptions.InvalidHashException;
import dev.hroberts.fileshare.fileupload.domain.domainexceptions.IDomainException;
import dev.hroberts.fileshare.fileupload.application.services.exceptions.InvalidHashAlgorithmException;
import dev.hroberts.fileshare.fileupload.application.services.exceptions.UploadAlreadyCompletedException;
import dev.hroberts.fileshare.fileupload.controllers.dtos.ChunkedFileUploadDto;
import dev.hroberts.fileshare.fileupload.controllers.dtos.CompleteChunkedUploadDto;
import dev.hroberts.fileshare.fileupload.controllers.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.fileupload.controllers.dtos.mappers.ChunkedFileUploadMapper;
import dev.hroberts.fileshare.fileupload.controllers.dtos.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.fileupload.application.services.FileService;
import dev.hroberts.fileshare.fileupload.controllers.resources.DeletableFileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("upload")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> initiateUpload(@RequestParam(required = true) String name, @RequestParam(required = false) String hash, @RequestParam(required = false) String hashAlgorithm, @RequestParam MultipartFile file) throws IDomainException, IOException, InvalidHashAlgorithmException, InvalidHashException {
        var fileInfo = fileService.uploadFile(name, hash, hashAlgorithm, file.getInputStream());
        return ResponseEntity.ok(SharedFileInfoMapper.mapToDto(fileInfo));
    }

    @PostMapping("initiate-multipart")
    public @ResponseBody ResponseEntity<ChunkedFileUploadDto> initiateMultipartUpload(@RequestBody ChunkedFileUploadDto request) throws IDomainException {
        var chunkedFileUpload = fileService.initiateChunkedUpload(request.name);
        return ResponseEntity.ok(ChunkedFileUploadMapper.mapToDto(chunkedFileUpload));
    }

    @PostMapping("{id}/add-chunk")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> uploadChunk(@PathVariable UUID id, @RequestParam(required = false) String hash, @RequestParam(required = false) String hashAlgorithm, @RequestParam MultipartFile file, @RequestParam int chunkIndex) throws IDomainException, InvalidHashAlgorithmException, UploadAlreadyCompletedException, InvalidHashException {
        try {
            fileService.saveChunk(id, chunkIndex, hash, hashAlgorithm, file.getInputStream());
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("{id}/complete")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> completeUpload(@PathVariable UUID id, @RequestBody(required = false) CompleteChunkedUploadDto request) throws InvalidHashAlgorithmException, UploadAlreadyCompletedException, InvalidHashException {
        try {
            String hash = null;
            String hashAlgorithm = null;
            if (request != null) {
                hash = request.hash;
                hashAlgorithm = request.hashAlgorithm;
            }

            var response = fileService.completeUpload(id, hash, hashAlgorithm);
            return ResponseEntity.ok(SharedFileInfoMapper.mapToDto(response));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}/info")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> getInfo(@PathVariable UUID id) {
        var response = fileService.getFileInfo(id);
        return ResponseEntity.ok(SharedFileInfoMapper.mapToDto(response));
    }

    @GetMapping(value = "{id}/download")
    public @ResponseBody ResponseEntity<Resource> download(@PathVariable UUID id) {
        try {
            var downloadableFile = fileService.getDownloadableFile(id);
            var response = new DeletableFileSystemResource(downloadableFile.filePath, downloadableFile.shouldDelete);
            var headers = new HttpHeaders();
            headers.setContentType(downloadableFile.mediaType);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadableFile.fileName);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            return ResponseEntity.ok().contentLength(response.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .headers(headers)
                    .body(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
