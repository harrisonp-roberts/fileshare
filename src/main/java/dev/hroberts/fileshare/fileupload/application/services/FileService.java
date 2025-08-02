package dev.hroberts.fileshare.fileupload.application.services;

import dev.hroberts.fileshare.fileupload.application.constants.HashStrategyEnum;
import dev.hroberts.fileshare.fileupload.application.services.exceptions.InvalidHashException;
import dev.hroberts.fileshare.fileupload.domain.ChunkedFileUpload;
import dev.hroberts.fileshare.fileupload.domain.DownloadableFile;
import dev.hroberts.fileshare.fileupload.domain.FileInfo;
import dev.hroberts.fileshare.fileupload.application.services.exceptions.UploadAlreadyCompletedException;
import dev.hroberts.fileshare.fileupload.domain.domainexceptions.IDomainException;
import dev.hroberts.fileshare.fileupload.domain.domainexceptions.FailedToSaveChunkException;
import dev.hroberts.fileshare.fileupload.application.repositories.IFileSystemRepository;
import dev.hroberts.fileshare.fileupload.application.repositories.IChunkedFileUploadRepository;
import dev.hroberts.fileshare.fileupload.application.repositories.IFileInfoRepository;
import dev.hroberts.fileshare.fileupload.application.services.exceptions.InvalidHashAlgorithmException;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileService {
    final String host;
    final IFileInfoRepository fileInfoRepository;
    final IChunkedFileUploadRepository chunkedUploadRepository;
    final IFileSystemRepository localFileStore;
    final AsyncFileService asyncFileService;

    public FileService(IFileInfoRepository fileInfoRepository, IChunkedFileUploadRepository fileUploadRepository, IFileSystemRepository localFileStore, @Value("${host}") String host, AsyncFileService asyncFileService) {
        this.fileInfoRepository = fileInfoRepository;
        this.chunkedUploadRepository = fileUploadRepository;
        this.localFileStore = localFileStore;
        this.host = host;
        this.asyncFileService = asyncFileService;
    }

    public ChunkedFileUpload initiateChunkedUpload(String name) throws IDomainException {
        var chunkedFileUpload = new ChunkedFileUpload(name);
        chunkedUploadRepository.save(chunkedFileUpload);
        return chunkedFileUpload;
    }

    public void saveChunk(UUID id, int chunkIndex, String hash, String hashAlgorithm, InputStream input) throws IDomainException, InvalidHashAlgorithmException, UploadAlreadyCompletedException, InvalidHashException {
        if ((hash == null && hashAlgorithm != null) || (hash != null && hashAlgorithm == null))
            throw new InvalidHashAlgorithmException("Both the hash and hash algorithm must be either provided or null");
        var chunkedUpload = chunkedUploadRepository.findById(id.toString()).orElseThrow();
        if (chunkedUpload.completing)
            throw new UploadAlreadyCompletedException("Chunks cannot be added to a completed upload");

        var chunkName = String.format("%s.%s", chunkedUpload.name, chunkIndex);

        try {
            localFileStore.write(id, chunkName, input);
        } catch (IOException ex) {
            throw new FailedToSaveChunkException();
        }

        if (hash != null) {
            validateFileHash(id, chunkName, hash, hashAlgorithm);
        }
    }

    public FileInfo completeUpload(UUID id, String hash, String hashAlgorithm) throws UploadAlreadyCompletedException, InvalidHashAlgorithmException, IOException, InvalidHashException {
        if ((hash == null && hashAlgorithm != null) || (hash != null && hashAlgorithm == null))
            throw new InvalidHashAlgorithmException("Both the hash and hash algorithm must be either provided or null");

        var chunkedFileUpload = chunkedUploadRepository.findById(id.toString()).orElseThrow();
        if (chunkedFileUpload.completing)
            throw new UploadAlreadyCompletedException("Upload has already been completed");
        var sharedFileInfo = new FileInfo(id, chunkedFileUpload.name, chunkedFileUpload.startTime);
        fileInfoRepository.save(sharedFileInfo);

        asyncFileService.processChunks(id, chunkedFileUpload, hash, hashAlgorithm);
        return sharedFileInfo;
    }

    public DownloadableFile getDownloadableFile(UUID id) throws FileNotFoundException {
        var fileInfo = fileInfoRepository.findById(id.toString()).orElseThrow();
        var filePath = localFileStore.load(id, fileInfo.fileName);
        return new DownloadableFile(fileInfo.fileName, filePath);
    }

    public String generateQrCode(UUID id) {
        fileInfoRepository.findById(id.toString()).orElseThrow();
        var downloadUrl = host + "/download/" + id;
        var qrCodeByteStream = QRCode.from(downloadUrl).withSize(250, 250).to(ImageType.PNG).stream();
        return new String(Base64.getEncoder().encode(qrCodeByteStream.toByteArray()));
    }

    public FileInfo getFileInfo(UUID id) {
        return fileInfoRepository.findById(id.toString()).orElse(null);
    }

    private AbstractMap.SimpleEntry<Integer, Integer> parseRange(String range) {
        if (range == null || range.isEmpty()) return null;
        String[] parts = range.split("=");
        String[] rangeParts = parts[1].split("-");
        if (parts.length != 2) return null;
        if (rangeParts.length < 1 || rangeParts.length > 2) return null;

        try {
            int start = Integer.parseInt(rangeParts[0]);
            int end = rangeParts.length == 2 ? Integer.parseInt(rangeParts[1]) : -1;
            if (start > end) return null;
            return new AbstractMap.SimpleEntry<>(start, end);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void validateFileHash(UUID id, String filename, String expectedHash, String hashAlgorithm) throws InvalidHashException {
        var hashStrategy = HashStrategyEnum.valueOf(hashAlgorithm).getHashStrategy();

        try {
            var hash = localFileStore.getHash(id, filename, hashStrategy);
            if (!hash.equals(expectedHash)) throw new InvalidHashException("Hash check failed");
        } catch (IOException e) {
            throw new InvalidHashException("Hash check failed");
        }
    }

    public FileInfo uploadFile(String name, String hash, String hashAlgorithm, InputStream inputStream) throws InvalidHashAlgorithmException, InvalidHashException {
        if ((hash == null && hashAlgorithm != null) || (hash != null && hashAlgorithm == null))
            throw new InvalidHashAlgorithmException("Both the hash and hash algorithm must be either provided or null");
        try {
            var fileInfo = new FileInfo(name, LocalDateTime.now());
            localFileStore.write(fileInfo.id, name, inputStream);

            if (hash != null) {
                validateFileHash(fileInfo.id, name, hash, hashAlgorithm);
            }

            fileInfoRepository.save(fileInfo);
            return fileInfo;
        } catch (IOException | IDomainException e) {
            return null;
        }
    }
}