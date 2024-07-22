package org.example.snippetservice.domains.testCases.controller;

import jakarta.validation.Valid;
import org.example.snippetservice.domains.UserService;
import org.example.snippetservice.domains.testCases.dto.CreateTestCaseDTO;
import org.example.snippetservice.domains.testCases.model.TestCase;
import org.example.snippetservice.domains.testCases.service.TestCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test-case")
public class TestCaseController {

    private final UserService userService;
    private final TestCaseService testCaseService;

    public TestCaseController(UserService userService, TestCaseService testCaseService) {
        this.userService = userService;
        this.testCaseService = testCaseService;
    }


    @PostMapping("/{owner-id}/{file-name}")
    public ResponseEntity<TestCase> createTestCase(
            @
            @RequestBody @Valid CreateTestCaseDTO testCaseDTO
    ) {
        String userId = userService.getUserId();
        //TestCase testCase = testCaseService.createTestCase(testCaseDTO,)
    }

    @GetMapping("/{owner-id}/{file-name}")
    public ResponseEntity<List<TestCase>> getSnippetTestCases() {
        return ResponseEntity.status(500).build();
    }

    @DeleteMapping("/{owner-id}/{file-name}")
    public ResponseEntity<?> deleteSnippetTestCase() {
        return ResponseEntity.status(500).build();
    }

    @GetMapping("/{owner-id}/{file-name}/{test-id}")
    public ResponseEntity<Boolean> executeTests() {
        return ResponseEntity.status(500).build();
    }
}
