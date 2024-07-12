package dev.hroberts.fileshare.application.services;

import dev.hroberts.fileshare.application.domain.ChunkedFileUpload;
import dev.hroberts.fileshare.application.domain.DownloadableFile;
import dev.hroberts.fileshare.application.domain.MultipartChunk;
import dev.hroberts.fileshare.application.domain.SharedFileInfo;
import dev.hroberts.fileshare.application.exceptions.ChunkAlreadyExistsException;
import dev.hroberts.fileshare.application.exceptions.ChunkSizeOutOfBoundsException;
import dev.hroberts.fileshare.application.exceptions.ChunkedUploadCompletedException;
import dev.hroberts.fileshare.application.repositories.ChunkedFileUploadRepository;
import dev.hroberts.fileshare.application.repositories.FileInfoRepository;
import dev.hroberts.fileshare.persistence.filestore.IFileStore;
import net.glxn.qrgen.javase.QRCode;
import net.glxn.qrgen.core.image.ImageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Service
public class UserFileService {
    final String host;
    final FileInfoRepository fileInfoRepository;
    final ChunkedFileUploadRepository chunkedUploadRepository;
    final IFileStore localFileStore;

    public UserFileService(FileInfoRepository fileInfoRepository, ChunkedFileUploadRepository fileUploadRepository, IFileStore localFileStore, @Value("${host}") String host) {
        this.fileInfoRepository = fileInfoRepository;
        this.chunkedUploadRepository = fileUploadRepository;
        this.localFileStore = localFileStore;
        this.host = host;
    }

    public UUID initiateChunkedUpload(String name, long size, int downloadLimit) {
        var multipartUpload = new ChunkedFileUpload(name, size, downloadLimit);
        chunkedUploadRepository.save(multipartUpload);
        return multipartUpload.id;
    }

    //todo fix the name generation/serialization
    public void saveChunk(UUID uploadId, long size, int chunkIndex, InputStream input) throws ChunkAlreadyExistsException, ChunkSizeOutOfBoundsException {
        var chunkedUpload = chunkedUploadRepository.findById(uploadId.toString()).orElseThrow();
        if (chunkedUpload.chunkExists(chunkIndex)) throw new ChunkAlreadyExistsException();
        if (chunkedUpload.currentSize + size > chunkedUpload.size) throw new ChunkSizeOutOfBoundsException();

        MultipartChunk chunk = new MultipartChunk(String.format("%s.%s", chunkedUpload.name, chunkIndex), size, chunkIndex);
        var actualSize = localFileStore.write(uploadId, chunk.name, input);

        if (actualSize != size) {
            localFileStore.deleteFileByName(uploadId, chunk.name);
            throw new ChunkSizeOutOfBoundsException();
        }

        chunkedUpload.addChunk(chunk);
        chunkedUploadRepository.save(chunkedUpload);
    }

    public SharedFileInfo completeUpload(UUID uploadId) throws ChunkedUploadCompletedException {
        var chunkedFileUpload = chunkedUploadRepository.findById(uploadId.toString()).orElseThrow();

        try {
            for (MultipartChunk chunk : chunkedFileUpload.listChunks()) {
                localFileStore.copy(uploadId, chunk.name, chunkedFileUpload.name, true);
            }
            chunkedFileUpload.listChunks().forEach(chunk -> localFileStore.deleteFileByName(uploadId, chunk.name));
            chunkedUploadRepository.save(chunkedFileUpload);

            var sharedFileInfo = new SharedFileInfo(uploadId, chunkedFileUpload.name, chunkedFileUpload.downloadLimit);
            fileInfoRepository.save(sharedFileInfo);
            return sharedFileInfo;
        } catch (IOException ex) {
            localFileStore.deleteFileByName(uploadId, chunkedFileUpload.name);
            throw new ChunkedUploadCompletedException();
        }

    }

    public DownloadableFile downloadFile(UUID uploadId) throws FileNotFoundException {
        var fileInfo = fileInfoRepository.findById(uploadId.toString()).orElseThrow();
        fileInfo.download();
        var filePath = localFileStore.load(uploadId, fileInfo.fileName);
        fileInfoRepository.save(fileInfo);
        return new DownloadableFile(fileInfo.fileName, filePath);
    }

    public String generateQrCode(UUID uploadId) {
        fileInfoRepository.findById(uploadId.toString()).orElseThrow();
        var downloadUrl = host + "/download/" + uploadId;
        var qrCodeByteStream = QRCode.from(downloadUrl).withSize(250, 250).to(ImageType.PNG).stream();
        return new String(Base64.getEncoder().encode(qrCodeByteStream.toByteArray()));
    }

    public SharedFileInfo getFileInfo(UUID uploadId) {
        return fileInfoRepository.findById(uploadId.toString()).orElse(null);
    }
}