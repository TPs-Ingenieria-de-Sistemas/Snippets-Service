package org.example.snippetservice.domains.testCases.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateTestCaseDTO {
	Long id;
	@NotBlank
	String testCaseName;
	List<String> input;
	List<String> output;
	String env;

	public CreateTestCaseDTO() {
	}

	public CreateTestCaseDTO(Long id, @NotBlank String testCaseName, List<String> input, List<String> output,
			String env) {
		this.id = id;
		this.testCaseName = testCaseName;
		this.input = input;
		this.output = output;
		this.env = env;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public List<String> getInput() {
		return input;
	}

	public void setInput(List<String> input) {
		this.input = input;
	}

	public List<String> getOutput() {
		return output;
	}

	public void setOutput(List<String> output) {
		this.output = output;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}
}
