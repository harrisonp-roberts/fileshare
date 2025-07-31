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
public class UploadView {
    private final FileService fileService;
    @Value("${host}")
    String host;

    public UploadView(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String index(Model model) {
        System.out.println("Setting host: " + host);
        model.addAttribute("host", host);
        return "upload";
    }

    @GetMapping("/complete/{id}")
    public String complete(@PathVariable UUID id, Model model) {
        var fileInfo = fileService.getFileInfo(id);
        if(fileInfo == null) return "redirect:/";
        var fileInfoDto = SharedFileInfoMapper.mapToDto(fileInfo);
        var qrCode = fileService.generateQrCode(id);
        model.addAttribute("fileInfo", fileInfo);
        model.addAttribute("qrCode", qrCode);
        model.addAttribute("host", host);
        return "complete";
    }
}
