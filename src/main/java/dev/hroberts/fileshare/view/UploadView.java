package dev.hroberts.fileshare.view;

import dev.hroberts.fileshare.application.services.UserFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class UploadView {
    @Value("${host}") String host;
    private final UserFileService userFileService;
    public UploadView(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute( "host", host);
        return "upload";
    }

    @GetMapping("/complete/{id}")
    public String complete(@PathVariable UUID id, Model model) {
        var fileInfo = userFileService.getFileInfo(id);
        var qrCode = userFileService.generateQrCode(id);
        model.addAttribute("fileInfo", fileInfo);
        model.addAttribute("qrCode", qrCode);
        model.addAttribute("host", host);
        return "complete";
    }
}
