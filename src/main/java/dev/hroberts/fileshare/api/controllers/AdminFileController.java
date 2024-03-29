package dev.hroberts.fileshare.api.controllers;

import dev.hroberts.fileshare.api.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.api.dtos.UploadFileByPathDto;
import dev.hroberts.fileshare.api.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.application.services.AdminFileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminFileController {
    private final AdminFileService fileAdminService;

    public AdminFileController(AdminFileService adminFileService) {
        this.fileAdminService = adminFileService;
    }

    @Deprecated
    @PostMapping(value = "/uploadByPath", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SharedFileInfoDto> uploadFileByPath(@RequestBody UploadFileByPathDto uploadFileByPathDto) {
        try {
            var fileInfo = fileAdminService.uploadByPath(uploadFileByPathDto);
            var response = SharedFileInfoMapper.MapDomainToDto(fileInfo);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
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
                .map(SharedFileInfoMapper::MapDomainToDto)
                .toList();
        return ResponseEntity.ok(response);
    }

}
