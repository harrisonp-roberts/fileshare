package dev.hroberts.fileshare.fileupload.infrastructure.util.hashing;

import java.io.InputStream;
import com.google.common.hash.Hashing;

public class CRC32HashStrategy implements IHashStrategy{
    @Override
    public String generateHash(InputStream inputStream) {
        try {
            return Hashing.crc32().hashBytes(inputStream.readAllBytes()).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate CRC32 hash using Guava", e);
        }
    }
}
