package dev.hroberts.fileshare.application.domain;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("MultipartChunk")
public class MultipartChunk {
    public String name;
    public int chunkIndex;
    public long size;

    public MultipartChunk(String name, long size, int chunkIndex) {
        this.name = name;
        this.chunkIndex = chunkIndex;
        this.size = size;
    }
}
