package dev.hroberts.fileshare.persistence.filestore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.UUID;

public interface IFileStore {
    long write(UUID uploadId, String name, InputStream input);

    void deleteFileByName(UUID uploadId, String fileName);

    void copyFileIn(UUID uploadId, String inputFilePath, String fileName) throws FileAlreadyExistsException, IOException;

    void copy(UUID uploadId, String source, String target, boolean append) throws IOException;

    Path load(UUID uploadId, String fileName) throws FileNotFoundException;
}
