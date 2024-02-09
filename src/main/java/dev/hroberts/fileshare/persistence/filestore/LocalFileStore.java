package dev.hroberts.fileshare.persistence.filestore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Component
public class LocalFileStore implements IFileStore {

    private final Path rootFilePath;
    private final Path cachePath;

    public LocalFileStore(@Value("${file.store.dir}") String fileStoreDir, @Value("${file.cache.dir}") String fileCacheDir) {
        this.rootFilePath = Path.of(fileStoreDir);
        this.cachePath = Path.of(fileCacheDir);
    }

    @Override
    public void storeFile(InputStream input, String fileName) {
        try {
            var destinationFile = rootFilePath.resolve(Paths.get(fileName))
                    .normalize()
                    .toAbsolutePath();
            Files.copy(input, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new RuntimeException("Error reading file");
        }
    }

    @Override
    public void deleteFileByName(String fileName) {
        var file = rootFilePath.resolve(fileName).toFile();
        if (file.exists()) file.delete();
    }

    @Override
    public void copyLocalFile(String inputFIle, String outputFile) throws IOException {
        var sourceFile = rootFilePath.resolve(inputFIle).toFile();
        var destFile = rootFilePath.resolve(outputFile).toFile();
        if (destFile.exists()) throw new FileAlreadyExistsException("file already exists");
        Files.copy(sourceFile.toPath(), destFile.toPath());
    }

    @Override
    public void copyFileIn(String inputFilePath, String fileName) throws IOException {
        Path sourcePath = Path.of(inputFilePath);
        Path destinationPath = rootFilePath.resolve(fileName);

        if (destinationPath.toFile().exists()) throw new FileAlreadyExistsException("File Already Exists");
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void copyFileOut(String inputFileName, String outputFilePath) throws IOException {
        Path sourcePath = rootFilePath.resolve(inputFileName);
        Path destinationPath = Path.of(outputFilePath);

        if (destinationPath.toFile().exists()) throw new FileAlreadyExistsException("File Already Exists");
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public Path cacheFile(String fileName, String outputFileName) {
        //todo return more robust CachedFile domain object that may contain download flag, etc
        Path sourcePath = rootFilePath.resolve(fileName);
        Path destinationPath = cachePath.resolve(fileName).resolve(outputFileName);
        var destinationFile = destinationPath.toFile();
        try {
            if (!destinationFile.exists()) Files.createDirectories(destinationPath.getParent());
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error copying file to cache");
        }

        return destinationPath;
    }
}