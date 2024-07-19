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
}
