package dev.hroberts.fileshare.application.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;

@Service
public class FileService {
    public static void secureDelete(File file) throws IOException {

    }

    public void deleteFile(String filePath) {
        var file = new File(filePath);
        try {
            if (file.exists()) {
                long length = file.length();
                SecureRandom random = new SecureRandom();
                RandomAccessFile raf = new RandomAccessFile(file, "rws");
                raf.seek(0);
                raf.getFilePointer();
                byte[] data = new byte[64];
                int pos = 0;
                while (pos < length) {
                    random.nextBytes(data);
                    raf.write(data);
                    pos += data.length;
                }
                raf.close();
                file.delete();
            }
        } catch (IOException ignored) {
        }
    }

    public boolean fileExists(String filePath) {
        File temp = new File(filePath);
        return temp.exists();
    }

    public boolean copyFile(String inputFilePath, String outputFilePath) {
        Path sourcePath = Path.of(inputFilePath);
        Path destinationPath = Path.of(outputFilePath);

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
