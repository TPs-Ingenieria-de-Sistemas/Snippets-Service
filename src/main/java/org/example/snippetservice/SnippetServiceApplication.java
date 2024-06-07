package org.example.snippetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnippetServiceApplication {

    public static void main(String[] args) {
        System.out.println("Running...");
        SpringApplication.run(SnippetServiceApplication.class, args);

    }

}
