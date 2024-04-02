package dev.hroberts.fileshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileshareApplication {
    /*
    TODO:
    - watch directory functionality
    - compression
    - hash checking on individual chunks and the whole
     */
    public static void main(String[] args) {
        SpringApplication.run(FileshareApplication.class, args);
    }

}
