package dev.hroberts.fileshare.persistence.repositories;

import dev.hroberts.fileshare.models.ChunkedFileUpload;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkedFileUploadRepository extends CrudRepository<ChunkedFileUpload, String> {
}
