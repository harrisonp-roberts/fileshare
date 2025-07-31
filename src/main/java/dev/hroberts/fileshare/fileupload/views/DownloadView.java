package dev.hroberts.fileshare.fileupload.views;

import dev.hroberts.fileshare.fileupload.controllers.dtos.mappers.SharedFileInfoMapper;
import dev.hroberts.fileshare.fileupload.application.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class DownloadView {
    private final FileService fileService;
    @Value("${host}")
    String host;

    public DownloadView(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download")
    public String download(Model model) {
        model.addAttribute("host", host);
        return "redirect:/";
    }

    @GetMapping("/download/{id}")
    public String download(@PathVariable UUID id, Model model) {
        var fileInfo = fileService.getFileInfo(id);
        if(fileInfo == null) return "redirect:/";
        var fileInfoDto = SharedFileInfoMapper.mapToDto(fileInfo);
        model.addAttribute("fileInfo", fileInfoDto);
        model.addAttribute("host", host);
        return "download";
    }
}
