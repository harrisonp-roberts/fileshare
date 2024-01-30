package dev.hroberts.fileshare.api.controllers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.application.services.FileAdminService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminFileController {
    private final FileAdminService fileAdminService;

    public AdminFileController(FileAdminService fileAdminService) {
        this.fileAdminService = fileAdminService;
    }

    @PostMapping(value = "/uploadByPath", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SharedFileInfoDto uploadFileByPath(@RequestBody SharedFileInfoDto sharedFileInfoDto) {
        return fileAdminService.uploadFileByPath(sharedFileInfoDto);
    }

    @PostMapping(value = "/purge")
    public @ResponseBody ResponseEntity<String> purgeFiles() {
        fileAdminService.purgeFiles();
        return ResponseEntity.ok("success");
    }

    @GetMapping(value = "/listAllFiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<SharedFileInfoDto> listAllFiles() {
        return fileAdminService.listAllFiles();
    }

}
