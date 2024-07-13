package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.application.domain.ChunkedFileUpload;
import dev.hroberts.fileshare.application.domain.DownloadableFile;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.application.exceptions.ChunkAlreadyExistsException;
import dev.hroberts.fileshare.application.exceptions.ChunkSizeOutOfBoundsException;
import dev.hroberts.fileshare.application.exceptions.ChunkedUploadCompletedException;
import dev.hroberts.fileshare.application.repositories.ChunkedFileUploadRepository;
import dev.hroberts.fileshare.application.repositories.FileInfoRepository;
import dev.hroberts.fileshare.persistence.filestore.IFileStore;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
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

    public ChunkedFileUpload initiateChunkedUpload(String name, long size, int downloadLimit) {
        var chunkedFileUpload = new ChunkedFileUpload(name, size, downloadLimit);
        chunkedUploadRepository.save(chunkedFileUpload);
        return chunkedFileUpload;
    }

    public void saveChunk(UUID id, long chunkSize, int chunkIndex, InputStream input) throws ChunkAlreadyExistsException, ChunkSizeOutOfBoundsException {
        var chunkedUpload = chunkedUploadRepository.findById(id.toString()).orElseThrow();
        var chunk = chunkedUpload.addChunk(chunkSize, chunkIndex);

        var actualSize = localFileStore.write(id, chunk.name, input);
        if (actualSize != chunkSize) {
            localFileStore.deleteFileByName(id, chunk.name);
            throw new ChunkSizeOutOfBoundsException();
        }

        chunkedUploadRepository.save(chunkedUpload);
    }

    public SharedFileInfo completeUpload(UUID id) throws ChunkedUploadCompletedException {
        var chunkedFileUpload = chunkedUploadRepository.findById(id.toString()).orElseThrow();
        var sharedFileInfo = new SharedFileInfo(id, chunkedFileUpload.name, chunkedFileUpload.downloadLimit);
        fileInfoRepository.save(sharedFileInfo);
        asyncFileService.processChunks(id, chunkedFileUpload);
        return sharedFileInfo;

    }

    public DownloadableFile downloadFile(UUID id) throws FileNotFoundException {
        var fileInfo = fileInfoRepository.findById(id.toString()).orElseThrow();
        fileInfo.download();
        var filePath = localFileStore.load(id, fileInfo.fileName);
        fileInfoRepository.save(fileInfo);
        return new DownloadableFile(fileInfo.fileName, filePath);
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