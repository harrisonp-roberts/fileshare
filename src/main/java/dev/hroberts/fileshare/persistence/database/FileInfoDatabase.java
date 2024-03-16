package dev.hroberts.fileshare.persistence.database;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoDatabase extends CrudRepository<SharedFileInfo, String> {
}
