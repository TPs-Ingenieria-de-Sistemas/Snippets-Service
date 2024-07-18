package org.example.snippetservice.domains.snippet.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/health")
public class Health {
    @GetMapping
    public String health() {
        return "OK";
    }

    public void storeSnippet() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("http://localhost:8080/v1/", "snippet", String.class);
        System.out.println(response);
    }
}
