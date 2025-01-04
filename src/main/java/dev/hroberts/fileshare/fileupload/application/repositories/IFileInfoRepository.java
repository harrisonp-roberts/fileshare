package dev.hroberts.fileshare.fileupload.application.repositories;

import dev.hroberts.fileshare.fileupload.domain.SharedFileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFileInfoRepository extends CrudRepository<SharedFileInfo, String> {
}