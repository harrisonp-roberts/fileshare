package dev.hroberts.fileshare.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DownloadView {
    @Value("${host}") String host;

    @GetMapping("/download")
    public String download(Model model) {
        model.addAttribute("host", host);
        return "download";
    }
}
