package dev.hroberts.fileshare.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadView {
    @Value("${host}") String host;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute( "host", host);
        return "upload";
    }
}
