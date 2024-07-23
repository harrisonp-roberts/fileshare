package dev.hroberts.fileshare.web.api;

import dev.hroberts.fileshare.web.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.web.dtos.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.services.AdminFileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminFileController {
//    private final AdminFileService fileAdminService;

//    public AdminFileController(AdminFileService adminFileService) {
//        this.fileAdminService = adminFileService;
//    }
//
//    public @ResponseBody ResponseEntity<String> purgeFiles() {
//        fileAdminService.purgeFiles();
//        return ResponseEntity.ok("success");
//    }
//
//    public @ResponseBody ResponseEntity<List<SharedFileInfoDto>> listAllFiles() {
//        var sharedFileInfoList = fileAdminService.listFiles();
//        var response = sharedFileInfoList.stream()
//                .map(SharedFileInfoMapper::mapToDto)
//                .toList();
//        return ResponseEntity.ok(response);
//    }

}
