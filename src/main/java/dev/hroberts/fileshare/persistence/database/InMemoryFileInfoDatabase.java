package dev.hroberts.fileshare.persistence.database;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryFileInfoDatabase implements IDatabase {
    private final ArrayList<SharedFileInfo> filePaths = new ArrayList<>();

    public SharedFileInfo saveFile(SharedFileInfo fileInfo) {
        filePaths.add(fileInfo);
        return fileInfo;
    }

    public List<SharedFileInfo> listFiles() {
        return List.copyOf(filePaths);
    }

    public boolean removeFile(UUID fileId) {
        return filePaths.removeIf(sharedFileInfo -> sharedFileInfo.fileId == fileId);
    }
}
