package dev.hroberts.fileshare.api.controllers;

import dev.hroberts.fileshare.api.dto.SharedFileInfoDto;
import dev.hroberts.fileshare.application.FileAdminService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin")
public class AdminFileController {
    private final FileAdminService fileAdminService;

    public AdminFileController(FileAdminService fileAdminService) {
        this.fileAdminService = fileAdminService;
    }

    @PostMapping(value = "/uploadByPath", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SharedFileInfoDto uploadFileByPath(@RequestBody SharedFileInfoDto fileInfoDto) {
        return fileAdminService.uploadFileByPath(fileInfoDto);
    }
}
