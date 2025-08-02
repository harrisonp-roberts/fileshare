package dev.hroberts.fileshare.integration.configuration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.testcontainers.RedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpClient;
import java.util.List;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(properties = { "redis.password=" })
@AutoConfigureMockMvc
public abstract class DatabaseBackedTest {
    @Autowired protected MockMvc mockMvc;
    static final RedisContainer REDIS_CONTAINER;
    static final HttpClient HTTP_CLIENT;

    public <T> T validateAndParseResponse(MvcResult result, Class<T> responseType) throws IOException {
        if (result.getResponse().getStatus() < 200 || result.getResponse().getStatus() > 299) {
            fail("Response status was not 2xx. Response status: " + result.getResponse().getStatus() + "\n Response Body: " + result.getResponse().getContentAsString() + "\n");
        }

        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), responseType);
    }

    public void validateResponse(MvcResult result) throws UnsupportedEncodingException {
        if (result.getResponse().getStatus() < 200 || result.getResponse().getStatus() > 299) {
            fail("Response status was not 2xx. Response status: " + result.getResponse().getStatus() + "\n Response Body: " + result.getResponse().getContentAsString() + "\n");
        }
    }

    public MvcResult httpPost(String url, Object body) throws Exception {
        var response = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body)));
        return response.andReturn();
    }

    public MvcResult httpPostMultipart(String url, MockMultipartFile file) throws Exception {
        var response = mockMvc.perform(MockMvcRequestBuilders.multipart(url).part(new MockPart("chunkIndex", "0".getBytes())).file(file));
        return response.andReturn();
    }

    public MvcResult httpPostMultipart(String url, MockMultipartFile file, List<MockPart> parts) throws Exception {
        var request = MockMvcRequestBuilders.multipart(url);
        for (var part : parts) {request = request.part(part);}
        if(file != null) {request = request.file(file);}
        return mockMvc.perform(request).andReturn();
    }

    public MvcResult httpPut(String url) throws Exception {
        var response = mockMvc.perform(put(url));
        return response.andReturn();
    }

    public MvcResult httpGet(String url) throws Exception {
        var response = mockMvc.perform(MockMvcRequestBuilders.get(url));
        return response.andReturn();
    }

    static {
        REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
        REDIS_CONTAINER.start();
        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
        HTTP_CLIENT = HttpClient.newBuilder().build();
    }
}
