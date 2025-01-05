package dev.hroberts.fileshare.fileupload.application.constants;

import dev.hroberts.fileshare.fileupload.infrastructure.util.hashing.CRC32HashStrategy;
import dev.hroberts.fileshare.fileupload.infrastructure.util.hashing.IHashStrategy;

public enum HashStrategyEnum {
    CRC_32(new CRC32HashStrategy()) {};
    private final IHashStrategy hashStrategy;

    HashStrategyEnum(IHashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
    }

    public IHashStrategy getHashStrategy() {
        return hashStrategy;
    }
}
