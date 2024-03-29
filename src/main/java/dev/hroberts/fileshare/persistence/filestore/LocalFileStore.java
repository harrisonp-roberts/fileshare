package dev.hroberts.fileshare.persistence.filestore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Component
public class LocalFileStore implements IFileStore {

    private final Path rootFilePath;

    public LocalFileStore(@Value("${file.store.dir}") String fileStoreDir) {
        this.rootFilePath = Path.of(fileStoreDir);
    }

    @Override
    public void save(InputStream input, String fileName) {
        try {
            var destinationFile = rootFilePath
                    .resolve(Paths.get(fileName))
                    .normalize()
                    .toAbsolutePath();

            if (!destinationFile.toFile().exists()) {
                destinationFile.toFile().createNewFile();
            }

            Files.write(destinationFile, input.readAllBytes(), StandardOpenOption.APPEND);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void deleteFileByName(String fileName) {
        var file = rootFilePath.resolve(fileName).toFile();
        if (file.exists()) file.delete();
    }

    @Override
    public void copyFileIn(String inputFilePath, String fileName) throws IOException {
        Path sourcePath = Path.of(inputFilePath);
        Path destinationPath = rootFilePath.resolve(fileName);

        if (destinationPath.toFile().exists()) throw new FileAlreadyExistsException("File Already Exists");
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public Path load(String fileName) throws FileNotFoundException {
        var filePath = rootFilePath.resolve(fileName);
        if(filePath.toFile().exists()) {
            return filePath;
        } else {
            throw new FileNotFoundException();
        }
    }
}