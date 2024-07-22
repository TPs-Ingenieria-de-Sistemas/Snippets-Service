package org.example.snippetservice.domains.testCases.repository;

import org.example.snippetservice.domains.testCases.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}
