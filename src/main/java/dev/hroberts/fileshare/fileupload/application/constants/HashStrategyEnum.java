package dev.hroberts.fileshare.fileupload.application.constants;

import dev.hroberts.fileshare.fileupload.infrastructure.util.hashing.CRC32HashStrategy;
import dev.hroberts.fileshare.fileupload.infrastructure.util.hashing.IHashStrategy;
import dev.hroberts.fileshare.fileupload.infrastructure.util.hashing.MD5HashStrategy;
import dev.hroberts.fileshare.fileupload.infrastructure.util.hashing.SHA256HashStrategy;

public enum HashStrategyEnum {
    MD5(new MD5HashStrategy()) {},
    CRC_32(new CRC32HashStrategy()) {},
    SHA_256(new SHA256HashStrategy());

    private final IHashStrategy hashStrategy;

    HashStrategyEnum(IHashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
    }

    public IHashStrategy getHashStrategy() {
        return hashStrategy;
    }
}
