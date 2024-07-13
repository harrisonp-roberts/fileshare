package dev.hroberts.fileshare.application.domain;

import dev.hroberts.fileshare.application.exceptions.ChunkAlreadyExistsException;
import dev.hroberts.fileshare.application.exceptions.ChunkSizeOutOfBoundsException;
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
    public long currentSize;
    public long size;
    public int downloadLimit;

    public HashMap<Integer, MultipartChunk> chunks;

    public ChunkedFileUpload(String name, long size, int downloadLimit) {
        id = UUID.randomUUID();
        chunks = new HashMap<>();

        this.name = name;
        this.currentSize = 0L;
        this.size = size;
        this.downloadLimit = downloadLimit;
    }

    public MultipartChunk addChunk(long chunkSize, int chunkIndex) throws ChunkAlreadyExistsException, ChunkSizeOutOfBoundsException {
        if (chunkExists(chunkIndex)) throw new ChunkAlreadyExistsException();
        if (currentSize + chunkSize > size) throw new ChunkSizeOutOfBoundsException();
        var chunkName = String.format("%s.%s", name, chunkIndex);
        var chunk = new MultipartChunk(chunkName, chunkSize, chunkIndex);
        chunks.put(chunkIndex, chunk);
        return chunk;
    }

    public boolean chunkExists(int chunkIndex) {
        return chunks.containsKey(chunkIndex);
    }

    public List<MultipartChunk> listChunks() {
        return chunks.values()
                .stream()
                .sorted(Comparator.comparing(o -> o.chunkIndex))
                .toList();
    }
}
