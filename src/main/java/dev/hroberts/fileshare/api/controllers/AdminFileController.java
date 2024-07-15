package dev.hroberts.fileshare.api.controllers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.api.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.application.services.AdminFileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminFileController {
    private final AdminFileService fileAdminService;

    public AdminFileController(AdminFileService adminFileService) {
        this.fileAdminService = adminFileService;
    }

    @PostMapping(value = "/purge")
    public @ResponseBody ResponseEntity<String> purgeFiles() {
        fileAdminService.purgeFiles();
        return ResponseEntity.ok("success");
    }

    @GetMapping(value = "/listAllFiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<List<SharedFileInfoDto>> listAllFiles() {
        var sharedFileInfoList = fileAdminService.listFiles();
        var response = sharedFileInfoList.stream()
                .map(SharedFileInfoMapper::mapToDto)
                .toList();
        return ResponseEntity.ok(response);
    }

}
