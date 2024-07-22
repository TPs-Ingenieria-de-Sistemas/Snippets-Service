package org.example.snippetservice.domains.testCases.service;

import java.util.List;
import java.util.Optional;
import org.example.snippetservice.domains.snippet.model.Snippet;
import org.example.snippetservice.domains.snippet.repository.SnippetRepository;
import org.example.snippetservice.domains.testCases.dto.CreateTestCaseDTO;
import org.example.snippetservice.domains.testCases.model.TestCase;
import org.example.snippetservice.domains.testCases.repository.TestCaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import TestCaseDTO.RunTestCaseDTO;

@Service
public class TestCaseService {
	private final TestCaseRepository testCaseRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	private final SnippetRepository snippetRepository;
	private final String printscriptUrl = "http://printscript:8081/";

	public TestCaseService(TestCaseRepository testCaseRepository, SnippetRepository snippetRepository) {
		this.testCaseRepository = testCaseRepository;
		this.snippetRepository = snippetRepository;
	}

	public ResponseEntity<TestCase> createTestCase(CreateTestCaseDTO createTestCaseDTO, String ownerId, String userId,
			String fileName) {
		Optional<Snippet> snippetOp = snippetRepository.findByUserIdAndName(ownerId, fileName);
		if (snippetOp.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		// Missing validate user permits

		TestCase finalTestCase;
		if (createTestCaseDTO.getId() == null) {
			String input = String.join(";", createTestCaseDTO.getInput());
			String output = String.join(";", createTestCaseDTO.getOutput());
			finalTestCase = this.testCaseRepository.save(new TestCase(ownerId, fileName,
					createTestCaseDTO.getTestCaseName(), input, output, createTestCaseDTO.getEnv()));
		} else {
			TestCase testCase = this.testCaseRepository.findById(createTestCaseDTO.getId()).orElseThrow();
			testCase.setTestCaseName(createTestCaseDTO.getTestCaseName());
			testCase.setInput(String.join(";", createTestCaseDTO.getInput()));
			testCase.setOutput(String.join(";", createTestCaseDTO.getOutput()));
			testCase.setEnv(createTestCaseDTO.getEnv());
			finalTestCase = this.testCaseRepository.save(testCase);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(finalTestCase);
	}

	public List<TestCase> getSnippetTestCases(String ownerId, String fileName) {
		return this.testCaseRepository.findByOwnerIdAndFileName(ownerId, fileName);
	}

	public void deleteSnippetTestCase(Long testId) {
		try {
			this.testCaseRepository.findById(testId).orElseThrow();
			this.testCaseRepository.deleteById(testId);
		} catch (Exception e) {
			throw new RuntimeException("Test case not found");
		}
	}

	public Boolean executeTests(Long testId) {
		TestCase testCase = this.testCaseRepository.findById(testId).orElseThrow();
		Snippet snippet = this.snippetRepository.findByUserIdAndName(testCase.getOwnerId(), testCase.getFileName()).orElseThrow();
		RunTestCaseDTO runTestCaseDTO = new RunTestCaseDTO(snippet.getContent(), testCase.getInput(),
				testCase.getOutput(), testCase.getEnv());
		try {
			return this.restTemplate.postForObject(printscriptUrl+"/test", runTestCaseDTO, Boolean.class);
		} catch (Exception e) {
			throw new RuntimeException("Error executing tests");
		}
	}
}
