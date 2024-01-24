package dev.hroberts.fileshare.persistence;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.persistence.interfaces.IDatabase;

import java.util.ArrayList;
public class InMemoryFileInfoDatabase implements IDatabase {
    private final ArrayList<SharedFileInfo> filePaths = new ArrayList<>();
    public SharedFileInfo saveFile(SharedFileInfo fileInfo) {
        filePaths.add(fileInfo);
        return fileInfo;
    }
}
