package dev.hroberts.fileshare.persistence.repositories;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.persistence.InMemoryFileInfoDatabase;
import org.springframework.stereotype.Repository;

@Repository
public class FileInfoRepository {
    final InMemoryFileInfoDatabase database;

    public FileInfoRepository(InMemoryFileInfoDatabase database) {
        this.database = database;
    }

    public SharedFileInfo saveFileInfo(SharedFileInfo fileInfo) {
        return database.saveFile(fileInfo);
    }
}
