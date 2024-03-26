package dev.hroberts.fileshare.api.controllers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.api.mappers.SharedFileInfoMapper;
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

@Controller
@RequestMapping("files")
public class UserFileController {
    private final UserFileService userFileService;

    public UserFileController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @PostMapping("/upload")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam int downloadLimit) {
        var sharedFileInfo = userFileService.uploadFile(file, downloadLimit);
        return ResponseEntity.ok(SharedFileInfoMapper.MapDomainToDto(sharedFileInfo));
    }

    @GetMapping(value = "/download/{fileId}")
    public @ResponseBody ResponseEntity<Resource> getFile(@PathVariable String fileId) {
        try {
            var downloadableFile = userFileService.downloadFile(fileId);
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
