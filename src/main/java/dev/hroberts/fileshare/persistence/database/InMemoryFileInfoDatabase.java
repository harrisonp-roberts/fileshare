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

    public boolean removeFile(String fileId) {
        return filePaths.removeIf(sharedFileInfo -> sharedFileInfo.fileId.equals(fileId));
    }

    public SharedFileInfo getById(String fileId) {
        var optionalFile = filePaths.stream().filter(fileInfo -> fileInfo.fileId.equals(fileId)).findFirst();
        return optionalFile.orElse(null);
    }
}
