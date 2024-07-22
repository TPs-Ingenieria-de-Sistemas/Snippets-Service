package org.example.snippetservice.domains.testCases.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.example.snippetservice.domains.testCases.dto.CreateTestCaseDTO;

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

    public TestCase(CreateTestCaseDTO testCaseDTO) {

    }

    public TestCase(){}

    public List<String> getInput() {
        return Arrays.stream(input.split(";")).toList();
    }

    public List<String> getOutput() {
        return  Arrays.stream(input.split(";")).toList();
    }
}
