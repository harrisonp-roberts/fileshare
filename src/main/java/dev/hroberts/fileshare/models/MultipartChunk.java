package dev.hroberts.fileshare.models;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("MultipartChunk")
public class MultipartChunk {
    public String name;
    public int chunkIndex;

    public MultipartChunk(String name, long size, int chunkIndex) {
        this.name = name;
        this.chunkIndex = chunkIndex;
    }
}
