package dev.hroberts.fileshare.integration;

import dev.hroberts.fileshare.fileupload.controllers.dtos.ChunkedFileUploadDto;
import dev.hroberts.fileshare.fileupload.controllers.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.integration.configuration.DatabaseBackedTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class FileControllerTests extends DatabaseBackedTest {

    @Test
    public void initiateUploadWithValidFileShouldSucceed() throws Exception {
        // arrange
        var request = new ChunkedFileUploadDto() {{ name = "test.txt"; }};

        // act
        var response = httpPost("/files/initiate-multipart", request);

        // assert
        var chunkedFileUpload = validateAndParseResponse(response, ChunkedFileUploadDto.class);
        assertNotNull(chunkedFileUpload);
        assertNotNull(chunkedFileUpload.id);
        assertTrue(chunkedFileUpload.startTime > 0);
        assertEquals("test.txt", chunkedFileUpload.name);
    }

    @Test
    public void addChunkToExistingUploadShouldSucceed() throws Exception {
        // arrange
        var fileUploadResponse = httpPost("/files/initiate-multipart", new ChunkedFileUploadDto() {{ name = "test.txt"; }});
        var fileUpload = validateAndParseResponse(fileUploadResponse, ChunkedFileUploadDto.class);
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test chunk".getBytes());

        // act
        var response = httpPostMultipart("/files/" + fileUpload.id + "/add-chunk", file);

        // assert
        validateResponse(response);
    }

    @Test
    public void completeUploadWithOneChunkShouldSucceed() throws Exception {
        // arrange
        var fileUploadResponse = httpPost("/files/initiate-multipart", new ChunkedFileUploadDto() {{ name = "test.txt"; }});
        var fileUpload = validateAndParseResponse(fileUploadResponse, ChunkedFileUploadDto.class);
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test chunk".getBytes());
        httpPostMultipart("/files/" + fileUpload.id + "/add-chunk", file);

        // act
        var response = httpPut("/files/" + fileUpload.id + "/complete");

        // assert
        var sharedFileInfo = validateAndParseResponse(response, SharedFileInfoDto.class);
        assertNotNull(sharedFileInfo);
        assertNotNull(sharedFileInfo.id);
        assertEquals("test.txt", sharedFileInfo.fileName);
        assertTrue(sharedFileInfo.uploadStart > 0);
        assertTrue(sharedFileInfo.uploadEnd > 0);
        assertTrue(sharedFileInfo.uploadEnd > sharedFileInfo.uploadStart);
    }

    @Test
    public void getFileInfoWhenFileExistsShouldSucceed() throws Exception {
        // arrange
        var fileUploadResponse = httpPost("/files/initiate-multipart", new ChunkedFileUploadDto() {{ name = "test.txt"; }});
        var fileUpload = validateAndParseResponse(fileUploadResponse, ChunkedFileUploadDto.class);
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test chunk".getBytes());
        httpPostMultipart("/files/" + fileUpload.id + "/add-chunk", file);
        httpPut("/files/" + fileUpload.id + "/complete");

        // act
        var response = httpGet("/files/" + fileUpload.id + "/info");

        // assert
        var sharedFileInfo = validateAndParseResponse(response, SharedFileInfoDto.class);
        assertNotNull(sharedFileInfo);
        assertNotNull(sharedFileInfo.id);
        assertEquals("test.txt", sharedFileInfo.fileName);
        assertTrue(sharedFileInfo.uploadStart > 0);
        assertTrue(sharedFileInfo.uploadEnd > 0);
        assertTrue(sharedFileInfo.uploadEnd > sharedFileInfo.uploadStart);
    }

    @Test
    public void testDownloadFileWhenFileExistsShouldSucceed() throws Exception {
        // arrange
        var contents = "test chunk";
        var fileUploadResponse = httpPost("/files/initiate-multipart", new ChunkedFileUploadDto() {{ name = "test.txt"; }});
        var fileUpload = validateAndParseResponse(fileUploadResponse, ChunkedFileUploadDto.class);
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, contents.getBytes());
        httpPostMultipart("/files/" + fileUpload.id + "/add-chunk", file);
        httpPut("/files/" + fileUpload.id + "/complete");

        // act
        var response = httpGet("/files/" + fileUpload.id + "/download");

        // assert
        validateResponse(response);
        assertEquals(contents, response.getResponse().getContentAsString());
    }

    @Test
    public void testDownloadFileAfterMultiChunkUploadShouldSucceed() throws Exception {
        // arrange
        var contents = "test chunk";
        var fileUploadResponse = httpPost("/files/initiate-multipart", new ChunkedFileUploadDto() {{ name = "test.txt"; }});
        var fileUpload = validateAndParseResponse(fileUploadResponse, ChunkedFileUploadDto.class);
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, contents.getBytes());
        var indexPart = new MockPart("chunkIndex", "0".getBytes());
        var parts = new ArrayList<MockPart>();
        parts.add(indexPart);

        httpPostMultipart("/files/" + fileUpload.id + "/add-chunk", file, parts);

        httpPut("/files/" + fileUpload.id + "/complete");

        // act
        var response = httpGet("/files/" + fileUpload.id + "/download");

        // assert
        validateResponse(response);
        assertEquals(contents, response.getResponse().getContentAsString());
    }

    @Test
    public void testHashedChunkUploadMD5() throws Exception {
        // arrange
        var contents = "test";
        var fileUploadResponse = httpPost("/files/initiate-multipart", new ChunkedFileUploadDto() {{ name = "test.txt"; }});
        var fileUpload = validateAndParseResponse(fileUploadResponse, ChunkedFileUploadDto.class);
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, contents.getBytes());
        var indexPart = new MockPart("chunkIndex", "0".getBytes());
        var hashAlgorithmPart = new MockPart("hashAlgorithm", "MD5".getBytes());
        var hash = new MockPart("hash", "098f6bcd4621d373cade4e832627b4f6".getBytes());
        var parts = new ArrayList<MockPart>();
        parts.add(indexPart);
        parts.add(hashAlgorithmPart);
        parts.add(hash);

        // act
        var response = httpPostMultipart("/files/" + fileUpload.id + "/add-chunk", file, parts);

        // assert
        validateResponse(response);
    }

    @Test
    public void testIncorrectHashedChunkUploadMD5ShouldFail() throws Exception {
        // arrange
        var contents = "test";
        var fileUploadResponse = httpPost("/files/initiate-multipart", new ChunkedFileUploadDto() {{ name = "test.txt"; }});
        var fileUpload = validateAndParseResponse(fileUploadResponse, ChunkedFileUploadDto.class);
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, contents.getBytes());
        var indexPart = new MockPart("chunkIndex", "0".getBytes());
        var hashAlgorithmPart = new MockPart("hashAlgorithm", "MD5".getBytes());
        var hash = new MockPart("hash", "test".getBytes());
        var parts = new ArrayList<MockPart>();
        parts.add(indexPart);
        parts.add(hashAlgorithmPart);
        parts.add(hash);

        // act
        var response = httpPostMultipart("/files/" + fileUpload.id + "/add-chunk", file, parts);

        // assert
        var status = response.getResponse().getStatus();
        assertTrue(status >= 400 && status < 500);
    }

    @Test
    public void testOneShotUpload() throws Exception {
        // arrange
        var contents = "test";
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, contents.getBytes());
        var namePart = new MockPart("name", "test.txt".getBytes());
        var hashAlgorithmPart = new MockPart("hashAlgorithm", "MD5".getBytes());
        var hash = new MockPart("hash", "098f6bcd4621d373cade4e832627b4f6".getBytes());
        var parts = new ArrayList<MockPart>();
        parts.add(namePart);
        parts.add(hashAlgorithmPart);
        parts.add(hash);

        // act
        var response = httpPostMultipart("/files/upload", file, parts);

        // assert
        var fileInfo = validateAndParseResponse(response, SharedFileInfoDto.class);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo.id);
        assertEquals("test.txt", fileInfo.fileName);
        assertTrue(fileInfo.uploadStart > 0);
        assertTrue(fileInfo.uploadEnd > 0);
        assertTrue(fileInfo.uploadEnd > fileInfo.uploadStart);
    }

    @Test
    public void downloadOneShotUpload() throws Exception {
        // arrange
        var contents = "test";
        var file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, contents.getBytes());
        var namePart = new MockPart("name", "test.txt".getBytes());
        var hashAlgorithmPart = new MockPart("hashAlgorithm", "MD5".getBytes());
        var hash = new MockPart("hash", "098f6bcd4621d373cade4e832627b4f6".getBytes());
        var parts = new ArrayList<MockPart>();
        parts.add(namePart);
        parts.add(hashAlgorithmPart);
        parts.add(hash);
        var fileInfoResponse = httpPostMultipart("/files/upload", file, parts);
        var fileInfo = validateAndParseResponse(fileInfoResponse, SharedFileInfoDto.class);

        // act
        var response = httpGet("/files/" + fileInfo.id + "/download");

        // assert
        validateResponse(response);
        assertEquals(contents, response.getResponse().getContentAsString());
    }
}
