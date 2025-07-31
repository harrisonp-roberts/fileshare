package dev.hroberts.fileshare.integration;

import dev.hroberts.fileshare.fileupload.controllers.dtos.ChunkedFileUploadDto;
import dev.hroberts.fileshare.fileupload.controllers.dtos.SharedFileInfoDto;
import dev.hroberts.fileshare.integration.configuration.DatabaseBackedTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestFileController extends DatabaseBackedTest {


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
}