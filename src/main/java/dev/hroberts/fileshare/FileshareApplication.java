package dev.hroberts.fileshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
public class FileshareApplication {
    //todo make sure I'm not mapping to the dto in the services on the application level
    /*
    TODO:
    - clean up repository/database implementation bs
    - watch directory functionality
    - compression
    - actual multipart files lol
     */
    public static void main(String[] args) {
        SpringApplication.run(FileshareApplication.class, args);
    }

}
