package dev.hroberts.fileshare.persistence.database;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RedisDatabase implements IDatabase {



    @Override
    public SharedFileInfo saveFile(SharedFileInfo fileInfo) {
        return null;
    }

    @Override
    public List<SharedFileInfo> listFiles() {
        return null;
    }

    @Override
    public boolean removeFile(String fileId) {
        return false;
    }

    @Override
    public SharedFileInfo getById(String fileId) {
        return null;
    }
}
