package dev.hroberts.fileshare.integration.configuration;


import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClients;
import com.redis.testcontainers.RedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.utility.DockerImageName;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class DatabaseBackedTest {
    @Autowired private MockMvc mockMvc;
    static final RedisContainer REDIS_CONTAINER;
    static final HttpClient HTTP_CLIENT;

    public HttpResponse postRequest(String url) {
        var request = HttpRequest.newBuilder().uri(java.net.URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
    }

    static {
        REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest"));
        REDIS_CONTAINER.start();
        HTTP_CLIENT = HttpClient.newBuilder().build();
    }
}
