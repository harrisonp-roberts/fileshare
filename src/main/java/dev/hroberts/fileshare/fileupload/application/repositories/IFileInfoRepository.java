package dev.hroberts.fileshare.fileupload.application.repositories;

import dev.hroberts.fileshare.fileupload.domain.FileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFileInfoRepository extends CrudRepository<FileInfo, String> {
}