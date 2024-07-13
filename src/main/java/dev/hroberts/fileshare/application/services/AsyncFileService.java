package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.application.domain.ChunkedFileUpload;
import dev.hroberts.fileshare.application.domain.MultipartChunk;
import dev.hroberts.fileshare.application.exceptions.ChunkedUploadCompletedException;
import dev.hroberts.fileshare.application.repositories.ChunkedFileUploadRepository;
import dev.hroberts.fileshare.application.repositories.FileInfoRepository;
import dev.hroberts.fileshare.persistence.filestore.IFileStore;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class AsyncFileService {
    final IFileStore localFileStore;
    final FileInfoRepository fileInfoRepository;
    final ChunkedFileUploadRepository chunkedUploadRepository;

    public AsyncFileService(IFileStore localFileStore, FileInfoRepository fileInfoRepository, ChunkedFileUploadRepository chunkedUploadRepository) {
        this.localFileStore = localFileStore;
        this.fileInfoRepository = fileInfoRepository;
        this.chunkedUploadRepository = chunkedUploadRepository;
    }

    @Async
    public void processChunks(UUID id, ChunkedFileUpload chunkedFileUpload) throws ChunkedUploadCompletedException {
        try {
            for (MultipartChunk chunk : chunkedFileUpload.listChunks()) {
                localFileStore.copy(id, chunk.name, chunkedFileUpload.name, true);
            }

            chunkedFileUpload.listChunks().forEach(chunk -> localFileStore.deleteFileByName(id, chunk.name));
            chunkedUploadRepository.delete(chunkedFileUpload);

            var sharedFileInfo = fileInfoRepository.findById(id.toString()).orElseThrow();
            sharedFileInfo.ready = true;

            fileInfoRepository.save(sharedFileInfo);
        } catch (IOException ex) {
            localFileStore.deleteFileByName(id, chunkedFileUpload.name);
            throw new ChunkedUploadCompletedException();
        }
    }
}
