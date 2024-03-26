package dev.hroberts.fileshare.persistence.filestore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

public interface IFileStore {
    void save(InputStream input, String fileName);

    void deleteFileByName(String fileName);

    void copyFileIn(String inputFilePath, String fileName) throws FileAlreadyExistsException, IOException;

    Path load(String fileName) throws FileNotFoundException;
}
