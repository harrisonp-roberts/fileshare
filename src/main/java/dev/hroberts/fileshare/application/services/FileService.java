package dev.hroberts.fileshare.application.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileService {
    public static void secureDelete(File file) throws IOException {
        //todo implement
    }

    public void deleteFile(String filePath) {
        var file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean fileExists(String filePath) {
        File temp = new File(filePath);
        return temp.exists();
    }

    public BufferedInputStream getFileByPath(String filePath) {
        try {
            var path = Paths.get(filePath);
            var inputStream = Files.newInputStream(path);
            return new BufferedInputStream(inputStream);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean copyFile(String inputFilePath, String outputFilePath) {
        Path sourcePath = Path.of(inputFilePath);
        Path destinationPath = Path.of(outputFilePath);
        try {
            var destinationFile = destinationPath.toFile();
            if(!destinationFile.exists()) destinationFile.createNewFile();

            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Path getPath(String filePath) {
        return Path.of(filePath);
    }
}
