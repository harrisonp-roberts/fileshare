package dev.hroberts.fileshare.persistence.database;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;

import java.util.List;

public interface IDatabase {
    //todo actually use this interface
    SharedFileInfo saveFile(SharedFileInfo fileInfo);
    List<SharedFileInfo> listFiles();
    boolean removeFile(String fileId);
    SharedFileInfo getById(String fileId);

}
