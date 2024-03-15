package dev.hroberts.fileshare.persistence.repositories;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.persistence.database.InMemoryFileInfoDatabase;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FileInfoRepository {
    final InMemoryFileInfoDatabase database;

    public FileInfoRepository(InMemoryFileInfoDatabase database) {
        this.database = database;
    }

    public SharedFileInfo saveFileInfo(SharedFileInfo fileInfo) {
        return database.saveFile(fileInfo);
    }

    public SharedFileInfo getFileById(String fileId) {
        return database.getById(fileId);
    }

    public List<SharedFileInfo> listFileInfo() {
        return database.listFiles();
    }

    public boolean deleteFileInfo(String fileId) {
        return database.removeFile(fileId);
    }
}
