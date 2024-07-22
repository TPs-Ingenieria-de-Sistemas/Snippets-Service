package org.example.snippetservice.domains.testCases.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.example.snippetservice.domains.UserService;
import org.example.snippetservice.domains.testCases.dto.CreateTestCaseDTO;
import org.example.snippetservice.domains.testCases.model.TestCase;
import org.example.snippetservice.domains.testCases.service.TestCaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<TestCase> createTestCase(@PathVariable("owner-id") String ownerId,
			@PathVariable("file-name") String fileName, @RequestBody @Valid CreateTestCaseDTO testCaseDTO) {
		String userId = userService.getUserId();
		return testCaseService.createTestCase(testCaseDTO, ownerId, userId, fileName);
	}

	@GetMapping("/{owner-id}/{file-name}")
	public ResponseEntity<List<TestCase>> getSnippetTestCases(@PathVariable("file-name") String fileName,
			@PathVariable("owner-id") String ownerId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(testCaseService.getSnippetTestCases(ownerId, fileName));
	}

	@DeleteMapping("/{test-id}")
	public ResponseEntity<?> deleteSnippetTestCase(@PathVariable("test-id") Long testId) {
		testCaseService.deleteSnippetTestCase(testId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{owner-id}/{file-name}/{test-id}")
	public ResponseEntity<Boolean> executeTests(@PathVariable("owner-id") String ownerId,
			@PathVariable("file-name") String fileName, @PathVariable("test-id") Long testId) {
		return ResponseEntity.ok(testCaseService.executeTests(ownerId, fileName, testId));
	}
}
