package dev.hroberts.fileshare.application.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    public boolean fileExists(String filePath) {
        File temp = new File(filePath);
        return temp.exists();
    }

    public boolean copyFile(String inputFilePath, String outputFilePath) {
        Path sourcePath = Path.of(inputFilePath);
        Path destinationPath = Path.of(outputFilePath);

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
