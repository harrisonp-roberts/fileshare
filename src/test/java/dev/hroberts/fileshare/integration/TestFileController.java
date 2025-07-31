package dev.hroberts.fileshare.integration;

import dev.hroberts.fileshare.fileupload.controllers.dtos.ChunkedFileUploadDto;
import dev.hroberts.fileshare.integration.configuration.DatabaseBackedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestFileController extends DatabaseBackedTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testController() throws Exception {
        var request = new ChunkedFileUploadDto() {{ name = "test.txt"; }};
        
        mockMvc.perform(post("/files/initiate-multipart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}