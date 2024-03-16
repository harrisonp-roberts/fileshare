package dev.hroberts.fileshare.api.controllers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.services.UserFileService;
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
        try {
            var sharedFileInfoDto = userFileService.storeFile(file, downloadLimit);
            return ResponseEntity.ok(sharedFileInfoDto);
        } catch (RuntimeException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/download/{fileId}")
    public @ResponseBody ResponseEntity<Resource> getFile(@PathVariable String fileId) {

        try {
            var response = userFileService.downloadFileById(fileId);
            var contentLength = response.contentLength();
            var headers = new HttpHeaders();
            var mediaType = MediaTypeFactory.getMediaType(response).orElse(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentType(mediaType);
            headers.add("Content-Disposition", "attachment; filename=" + response.getFilename());

            return ResponseEntity.ok().contentLength(contentLength).contentType(MediaType.APPLICATION_OCTET_STREAM).headers(headers).body(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

}
