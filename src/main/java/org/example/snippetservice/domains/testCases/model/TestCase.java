package org.example.snippetservice.domains.testCases.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Arrays;
import java.util.List;

@Entity
public class TestCase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String ownerId;
	String fileName;
	String testCaseName;
	String input;
	String output;
	String env;

	public TestCase(String ownerId, String fileName, String testCaseName, String input, String output, String env) {
		this.ownerId = ownerId;
		this.fileName = fileName;
		this.testCaseName = testCaseName;
		this.input = input;
		this.output = output;
		this.env = env;
	}

	public TestCase() {
	}

	public List<String> getInput() {
		return Arrays.stream(input.split(";")).toList();
	}

	public List<String> getOutput() {
		return Arrays.stream(input.split(";")).toList();
	}

	public Long getId() {
		return id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public String getFileName() {
		return fileName;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public String getEnv() {
		return env;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
