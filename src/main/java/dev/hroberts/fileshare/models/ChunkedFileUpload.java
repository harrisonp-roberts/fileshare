package dev.hroberts.fileshare.models;

import dev.hroberts.fileshare.models.exceptions.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RedisHash("ChunkedFileUpload")
public class ChunkedFileUpload {
    @Id
    public UUID id;
    public String name;
    public long size;
    public int downloadLimit;

    public ChunkedFileUpload(String name, long size, int downloadLimit) throws DomainException {
        if(name == null || name.isEmpty()) throw new InvalidFileNameException();
        if(size <= 0) throw new InvalidFileSizeException();

        id = UUID.randomUUID();
        this.name = name;
        this.size = size;

        if(downloadLimit < 1) {
            this.downloadLimit = -1;
        } else {
            this.downloadLimit = downloadLimit;
        }
    }

    public MultipartChunk addChunk(long chunkSize, int chunkIndex) throws ChunkAlreadyExistsException, ChunkSizeOutOfBoundsException {
        var chunkName = String.format("%s.%s", name, chunkIndex);
        return new MultipartChunk(chunkName, chunkSize, chunkIndex);
    }
}
