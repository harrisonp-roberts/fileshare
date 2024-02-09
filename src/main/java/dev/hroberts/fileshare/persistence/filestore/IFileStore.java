package dev.hroberts.fileshare.persistence.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

public interface IFileStore {
    void storeFile(InputStream input, String fileName);

    void deleteFileByName(String fileName);

    void copyLocalFile(String inputFIle, String outputFIle) throws FileAlreadyExistsException, IOException;

    void copyFileIn(String inputFilePath, String fileName) throws FileAlreadyExistsException, IOException;

    void copyFileOut(String inputFIleName, String outputFilePath) throws IOException;

    Path cacheFile(String fileName, String outputFileName);
}
