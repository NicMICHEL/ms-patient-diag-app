package com.nm.notesms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NotesMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesMsApplication.class, args);
    }

}
