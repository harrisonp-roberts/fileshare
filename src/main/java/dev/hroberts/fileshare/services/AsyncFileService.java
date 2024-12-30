package dev.hroberts.fileshare.services;

import dev.hroberts.fileshare.models.ChunkedFileUpload;
import dev.hroberts.fileshare.persistence.repositories.ChunkedFileUploadRepository;
import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import dev.hroberts.fileshare.persistence.files.IFileStore;
import dev.hroberts.fileshare.services.exceptions.UploadAlreadyCompletedException;
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
    public void processChunks(UUID id, ChunkedFileUpload chunkedFileUpload) throws IOException {
        try {
            var files = localFileStore.listFiles(id);
            for (var chunk : localFileStore.listFiles(id)) {
                localFileStore.copy(id, chunk.getFileName().toString(), chunkedFileUpload.name, true);
            }

            files.forEach(chunk -> localFileStore.deleteFileByName(id, chunk.getFileName().toString()));
            chunkedUploadRepository.delete(chunkedFileUpload);

            var sharedFileInfo = fileInfoRepository.findById(id.toString()).orElseThrow();
            var completedFile = localFileStore.load(id, sharedFileInfo.fileName);

            sharedFileInfo.ready = true;
            sharedFileInfo.fileSize = completedFile.toFile().length();

            fileInfoRepository.save(sharedFileInfo);
        } catch (IOException ex) {
            localFileStore.deleteFileByName(id, chunkedFileUpload.name);
            throw ex;
        }
    }
}
