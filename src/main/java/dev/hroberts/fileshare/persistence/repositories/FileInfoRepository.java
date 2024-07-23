package dev.hroberts.fileshare.persistence.repositories;

import dev.hroberts.fileshare.models.SharedFileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends CrudRepository<SharedFileInfo, String> {
}