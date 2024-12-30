package dev.hroberts.fileshare.web.api;

import dev.hroberts.fileshare.models.SharedFileInfo;
import dev.hroberts.fileshare.models.exceptions.DomainException;
import dev.hroberts.fileshare.services.exceptions.InvalidHashAlgorithmException;
import dev.hroberts.fileshare.services.exceptions.UploadAlreadyCompletedException;
import dev.hroberts.fileshare.web.dtos.ChunkedFileUploadDto;
import dev.hroberts.fileshare.web.dtos.CompleteChunkedUploadDto;
import dev.hroberts.fileshare.web.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.web.dtos.mappers.ChunkedFileUploadMapper;
import dev.hroberts.fileshare.web.dtos.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.services.UserFileService;
import dev.hroberts.fileshare.web.resources.DeletableFileSystemResource;
import jakarta.servlet.http.HttpServletResponse;
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
public class UserFileController {
    private final UserFileService userFileService;

    public UserFileController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @PostMapping("/initiate-multipart")
    public @ResponseBody ResponseEntity<ChunkedFileUploadDto> initiateMultipartUpload(@RequestBody ChunkedFileUploadDto request) throws DomainException {
        var chunkedFileUpload = userFileService.initiateChunkedUpload(request.name, request.downloadLimit);
        return ResponseEntity.ok(ChunkedFileUploadMapper.mapToDto(chunkedFileUpload));
    }

    @PostMapping("/upload/{id}")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> uploadChunk(@PathVariable UUID id, @RequestParam(required = false) String hash, @RequestParam(required = false) String hashAlgorithm, @RequestParam MultipartFile file, @RequestParam int chunkIndex) throws DomainException, InvalidHashAlgorithmException, UploadAlreadyCompletedException {
        try {
            userFileService.saveChunk(id, chunkIndex, hash, hashAlgorithm, file.getInputStream());
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/complete/{id}")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> completeUpload(@PathVariable UUID id, @RequestBody(required = false) CompleteChunkedUploadDto request) throws InvalidHashAlgorithmException, UploadAlreadyCompletedException {
        try {
            String hash = null;
            String hashAlgorithm = null;
            if (request != null) {
                hash = request.hash;
                hashAlgorithm = request.hashAlgorithm;
            }

            var response = userFileService.completeUpload(id, hash, hashAlgorithm);
            return ResponseEntity.ok(SharedFileInfoMapper.mapToDto(response));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/info/{id}")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> getInfo(@PathVariable UUID id) {
        var response = userFileService.getFileInfo(id);
        return ResponseEntity.ok(SharedFileInfoMapper.mapToDto(response));
    }

    @GetMapping(value = "/download/{id}")
    public @ResponseBody ResponseEntity<Resource> download(@PathVariable UUID id, HttpServletResponse servletResponse) {
        try {
            var downloadableFile = userFileService.getDownloadableFile(id);
            var response = new DeletableFileSystemResource(downloadableFile.filePath, downloadableFile.shouldDelete);
            var headers = new HttpHeaders();
            headers.setContentType(downloadableFile.mediaType);
            headers.add("Content-Disposition", "attachment; filename=" + downloadableFile.fileName);

            return ResponseEntity.ok().contentLength(response.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .headers(headers)
                    .body(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
