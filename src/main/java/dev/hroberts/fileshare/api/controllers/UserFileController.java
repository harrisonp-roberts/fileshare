package dev.hroberts.fileshare.api.controllers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.services.UserFileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("files")
public class UserFileController {

    private final UserFileService userFileService;

    public UserFileController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @PostMapping("/upload")
    public @ResponseBody ResponseEntity<SharedFileInfoDto> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam int maxUploads) throws IOException {
        //todo remove exception from method signature, handle this exception in the fileservice and have it throw something else.
        var sharedFileInfoDto = userFileService.storeFile(file, maxUploads);
        return ResponseEntity.ok(sharedFileInfoDto);
    }

    @GetMapping(value = "/download/{fileId}")
    public @ResponseBody ResponseEntity<Resource> getFile(@PathVariable UUID fileId) throws FileNotFoundException {

        try {
            var response = userFileService.downloadFileById(fileId);
            var contentLength = response.contentLength();
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("Content-Disposition", "attachment; filename=" + response.getFilename());

            return ResponseEntity.ok()
                    .contentLength(contentLength)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .headers(headers)
                    .body(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

}
