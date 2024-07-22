package org.example.snippetservice.domains.testCases.dto;

import java.util.List;

public class RunTestCaseDTO {
	String content;
	List<String> input;
	List<String> output;
	String env;

	public RunTestCaseDTO() {
	}

	public RunTestCaseDTO(String content, List<String> input, List<String> output, String env) {
		this.content = content;
		this.input = input;
		this.output = output;
		this.env = env;
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
