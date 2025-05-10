package dev.hroberts.fileshare.fileupload.infrastructure.util.hashing;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5HashStrategy implements IHashStrategy {
    @Override
    public String generateHash(InputStream inputStream) {
        try {
            return md5Hex(inputStream);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String md5Hex(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        // wrap the stream so it automatically updates the digest as you read
        try (DigestInputStream dis = new DigestInputStream(inputStream, md)) {
            byte[] buffer = new byte[8 * 1024];
            // read through the entire stream to update the digest
            while (dis.read(buffer) != -1) {
                // nothing else to do here; DigestInputStream updates md for us
            }
        }

        byte[] digest = md.digest();
        // convert to hex
        StringBuilder sb = new StringBuilder(2 * digest.length);
        for (byte b : digest) {
            // "%02x" means “two hex digits, zero-padded, lowercase”
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
