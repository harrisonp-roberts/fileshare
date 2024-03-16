package dev.hroberts.fileshare.persistence.repositories;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.persistence.database.FileInfoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileInfoRepository {
    private final FileInfoDatabase database;

    @Autowired
    public FileInfoRepository(FileInfoDatabase database){
        this.database = database;
    }

    public SharedFileInfo saveFileInfo(SharedFileInfo fileInfo) {
        return database.save(fileInfo);
    }

    public SharedFileInfo getFileById(String fileId) {
        return database.findById(fileId).orElseThrow();
    }

    public List<SharedFileInfo> listFileInfo() {
        var iterator = database.findAll();
        var list = new ArrayList<SharedFileInfo>();
        iterator.forEach(list::add);
        return list;
    }

    public void deleteFileInfo(String fileId) {
        database.deleteById(fileId);
    }
}