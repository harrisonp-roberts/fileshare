package dev.hroberts.fileshare.view;

import dev.hroberts.fileshare.application.services.UserFileService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class DownloadView {
    @Value("${host}") String host;

    private final UserFileService userFileService;

    public DownloadView(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @GetMapping("/download")
    public String download(Model model) {
        model.addAttribute("host", host);
        return "download";
    }

    @GetMapping("/download/{id}")
    public String download(@PathVariable UUID id, Model model) {
        var fileInfo = userFileService.getFileInfo(id);
        model.addAttribute("fileInfo", fileInfo);
        model.addAttribute("host", host);
        return "download";
    }
}
