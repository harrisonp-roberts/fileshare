package dev.hroberts.fileshare.application.domain;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("MultipartChunk")
public class MultipartChunk {
    public String name;
    public int position;
    public long size;

    public MultipartChunk(String name, long size, int position) {
        this.name = name;
        this.position = position;
        this.size = size;
    }
}
