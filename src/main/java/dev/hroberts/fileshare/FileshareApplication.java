package dev.hroberts.fileshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
public class FileshareApplication {
    /*
    TODO:
    - watch directory functionality
    - compression
    - actual multipart files lol
     */
    public static void main(String[] args) {
        SpringApplication.run(FileshareApplication.class, args);
    }

}
