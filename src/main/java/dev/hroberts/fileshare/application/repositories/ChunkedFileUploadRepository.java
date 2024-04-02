package dev.hroberts.fileshare.application.repositories;

import dev.hroberts.fileshare.application.domain.ChunkedFileUpload;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkedFileUploadRepository extends CrudRepository<ChunkedFileUpload, String> {
}
