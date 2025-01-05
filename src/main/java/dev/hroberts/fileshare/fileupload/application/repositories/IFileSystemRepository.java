package dev.hroberts.fileshare.fileupload.application.repositories;

import dev.hroberts.fileshare.fileupload.domain.domainexceptions.IDomainException;
import dev.hroberts.fileshare.fileupload.infrastructure.util.hashing.IHashStrategy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface IFileSystemRepository {
    long write(UUID id, String name, InputStream input) throws IOException, IDomainException;

    List<Path> listFiles(UUID id);

    void deleteFileByName(UUID id, String fileName);

    void copyFileIn(UUID id, String inputFilePath, String fileName) throws IOException;

    void copy(UUID id, String source, String target, boolean append) throws IOException;

    Path load(UUID id, String fileName) throws FileNotFoundException;

    String getHash(UUID id, String fileName, IHashStrategy hashStrategy) throws IOException;
}
