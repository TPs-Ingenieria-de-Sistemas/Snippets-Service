package org.example.snippetservice.domains.testCases.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class CreateTestCaseDTO {
    @NotBlank
    String testCaseName;
    List<String> input;
    List<String> output;
    String env;
}
