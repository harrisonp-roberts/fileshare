package dev.hroberts.fileshare.fileupload.infrastructure.util.hashing;

import java.io.InputStream;
import java.nio.file.Path;

public interface IHashStrategy {
    public String generateHash(InputStream inputStream);
}
