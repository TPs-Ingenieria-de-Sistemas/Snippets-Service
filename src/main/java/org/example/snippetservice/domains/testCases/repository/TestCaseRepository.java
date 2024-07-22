package org.example.snippetservice.domains.testCases.repository;

import java.util.List;

import org.example.snippetservice.domains.testCases.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
	List<TestCase> findByOwnerId(String ownerId);
	List<TestCase> findByOwnerIdAndFileName(String ownerId, String fileName);
	List<TestCase> findByOwnerIdAndFileNameAndTestCaseName(String ownerId, String fileName, String testCaseName);
	void deleteByOwnerIdAndFileNameAndTestCaseName(String ownerId, String fileName, String testCaseName);
	void deleteByOwnerIdAndFileName(String ownerId, String fileName);
	void deleteByOwnerId(String ownerId);
    TestCase getById(Long id);
}
