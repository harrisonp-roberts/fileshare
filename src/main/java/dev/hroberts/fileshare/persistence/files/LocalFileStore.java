package dev.hroberts.fileshare.persistence.files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@Component
public class LocalFileStore implements IFileStore {

    private final Path rootFilePath;

    public LocalFileStore(@Value("${file.store.dir}") String fileStoreDir) {
        this.rootFilePath = Path.of(fileStoreDir);
    }

    @Override
    public long write(UUID id, String name, InputStream input) {
        try {
            var destinationDir = rootFilePath.resolve(id.toString());
            var destinationFile = destinationDir
                    .resolve(Paths.get(name))
                    .normalize()
                    .toAbsolutePath();

            if (!destinationFile.toFile().exists()) {
                Files.createDirectories(destinationDir);
                Files.createFile(destinationFile);
            }

            Files.write(destinationFile, input.readAllBytes(), StandardOpenOption.APPEND);
            return Files.size(destinationFile);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void deleteFileByName(UUID id, String fileName) {
        var file = rootFilePath.resolve(id.toString()).resolve(fileName).toFile();
        if (file.exists()) file.delete();
    }

    @Override
    public void copyFileIn(UUID id, String inputFilePath, String fileName) throws IOException {
        Path sourcePath = Path.of(inputFilePath);
        Path destinationPath = rootFilePath.resolve(id.toString()).resolve(fileName);

        if (destinationPath.toFile().exists()) throw new FileAlreadyExistsException("File Already Exists");
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void copy(UUID id, String source, String target, boolean append) throws IOException {
        Path sourcePath = rootFilePath.resolve(id.toString()).resolve(source);
        Path targetPath = rootFilePath.resolve(id.toString()).resolve(target);

        if (!sourcePath.toFile().exists()) throw new FileNotFoundException("Source file not found");

        if (!targetPath.toFile().exists()) {
            Files.createDirectories(targetPath.getParent());
            Files.createFile(targetPath);
        }

        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(targetPath.toFile(), append));
             InputStream input = new BufferedInputStream(new FileInputStream(sourcePath.toFile()))) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public Path load(UUID id, String fileName) throws FileNotFoundException {
        var filePath = rootFilePath
                .resolve(id.toString())
                .resolve(fileName);
        if (filePath.toFile().exists()) {
            return filePath;
        } else {
            throw new FileNotFoundException();
        }
    }
}