package dev.hroberts.fileshare.application.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/*
TODO:
Need to move this out of the application layer and into persistence layer
 */

@Service
public class FileManipulationService {
    //todo refactor all the other places that reference a filepath directly here.
    //They're mostly in the admin service and user service, which shouldn't really need to know details of fil system implementation
    private final Path rootLocation = Path.of("/home/hroberts/files");
    private final Path tempLocation = Path.of("/home/hroberts/.cache/fileshare");

    public static void secureDelete(File file) throws IOException {
        //todo implement
    }

    public String storeFile(MultipartFile file, UUID fileId) {
        try (InputStream inputStream = file.getInputStream()){
            //todo doesn't throw if a file is empty
            var destinationFile = rootLocation.resolve(Paths.get(fileId.toString()))
                    .normalize()
                    .toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new SecurityException("Cannot store a file outside of current directory");
            }

            Files.copy(inputStream, destinationFile);

            return destinationFile.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file");
        }
    }

    public void deleteFile(String filePath) {
        var file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public void copyFile(String inputFilePath, String outputFilePath) throws IOException {
        var sourceFile = new File(inputFilePath);
        var destinationFile = new File(outputFilePath);
        destinationFile.getParentFile().mkdirs();
        destinationFile.createNewFile();
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
