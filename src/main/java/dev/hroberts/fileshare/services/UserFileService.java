package dev.hroberts.fileshare.services;

import dev.hroberts.fileshare.models.ChunkedFileUpload;
import dev.hroberts.fileshare.models.DownloadableFile;
import dev.hroberts.fileshare.models.SharedFileInfo;
import dev.hroberts.fileshare.models.exceptions.ChunkAlreadyExistsException;
import dev.hroberts.fileshare.models.exceptions.DomainException;
import dev.hroberts.fileshare.models.exceptions.FailedToSaveChunkException;
import dev.hroberts.fileshare.persistence.files.IFileStore;
import dev.hroberts.fileshare.persistence.repositories.ChunkedFileUploadRepository;
import dev.hroberts.fileshare.persistence.repositories.FileInfoRepository;
import dev.hroberts.fileshare.services.exceptions.ChunkedUploadCompletedException;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class UserFileService {
    final String host;
    final FileInfoRepository fileInfoRepository;
    final ChunkedFileUploadRepository chunkedUploadRepository;
    final IFileStore localFileStore;
    final AsyncFileService asyncFileService;

    public UserFileService(FileInfoRepository fileInfoRepository, ChunkedFileUploadRepository fileUploadRepository, IFileStore localFileStore, @Value("${host}") String host, AsyncFileService asyncFileService) {
        this.fileInfoRepository = fileInfoRepository;
        this.chunkedUploadRepository = fileUploadRepository;
        this.localFileStore = localFileStore;
        this.host = host;
        this.asyncFileService = asyncFileService;
    }

    public ChunkedFileUpload initiateChunkedUpload(String name, int downloadLimit) throws DomainException {
        var chunkedFileUpload = new ChunkedFileUpload(name, downloadLimit);
        chunkedUploadRepository.save(chunkedFileUpload);
        return chunkedFileUpload;
    }

    public void saveChunk(UUID id, int chunkIndex, String hash, InputStream input) throws DomainException {
        var chunkedUpload = chunkedUploadRepository.findById(id.toString()).orElseThrow();
        if (chunkedUpload.completing) throw new ChunkAlreadyExistsException();
        var chunkName = String.format("%s.%s", chunkedUpload.name, chunkIndex);
        try {
            localFileStore.write(id, chunkName, input);
        } catch (IOException ex) {
            throw new FailedToSaveChunkException();
        }
    }

    public SharedFileInfo completeUpload(UUID id) throws ChunkedUploadCompletedException {
        var chunkedFileUpload = chunkedUploadRepository.findById(id.toString()).orElseThrow();
        var sharedFileInfo = new SharedFileInfo(id, chunkedFileUpload.name, chunkedFileUpload.downloadLimit, chunkedFileUpload.startTime);
        fileInfoRepository.save(sharedFileInfo);
        asyncFileService.processChunks(id, chunkedFileUpload);
        return sharedFileInfo;
    }

    public DownloadableFile getDownloadableFile(UUID id) throws FileNotFoundException {
        var fileInfo = fileInfoRepository.findById(id.toString()).orElseThrow();
        var filePath = localFileStore.load(id, fileInfo.fileName);
        fileInfo.download();
        var shouldDelete = fileInfo.remainingDownloads == 0;

        if (shouldDelete) {
            fileInfoRepository.delete(fileInfo);
        } else {
            fileInfoRepository.save(fileInfo);
        }

        return new DownloadableFile(fileInfo.fileName, filePath, shouldDelete);
    }

    public String generateQrCode(UUID id) {
        fileInfoRepository.findById(id.toString()).orElseThrow();
        var downloadUrl = host + "/download/" + id;
        var qrCodeByteStream = QRCode.from(downloadUrl).withSize(250, 250).to(ImageType.PNG).stream();
        return new String(Base64.getEncoder().encode(qrCodeByteStream.toByteArray()));
    }

    public SharedFileInfo getFileInfo(UUID id) {
        return fileInfoRepository.findById(id.toString()).orElse(null);
    }
}