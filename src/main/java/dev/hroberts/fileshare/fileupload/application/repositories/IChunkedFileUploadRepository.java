package dev.hroberts.fileshare.fileupload.application.repositories;

import dev.hroberts.fileshare.fileupload.domain.ChunkedFileUpload;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChunkedFileUploadRepository extends CrudRepository<ChunkedFileUpload, String> {
}
