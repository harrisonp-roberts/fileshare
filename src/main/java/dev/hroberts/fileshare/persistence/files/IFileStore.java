package dev.hroberts.fileshare.persistence.files;

import dev.hroberts.fileshare.models.exceptions.DomainException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface IFileStore {
    long write(UUID id, String name, InputStream input) throws IOException, DomainException;

    List<Path> listFiles(UUID id);

    void deleteFileByName(UUID id, String fileName);

    void copyFileIn(UUID id, String inputFilePath, String fileName) throws IOException;

    void copy(UUID id, String source, String target, boolean append) throws IOException;

    Path load(UUID id, String fileName) throws FileNotFoundException;
}
