package dev.hroberts.fileshare.application.repositories;

import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends CrudRepository<SharedFileInfo, String> {
}