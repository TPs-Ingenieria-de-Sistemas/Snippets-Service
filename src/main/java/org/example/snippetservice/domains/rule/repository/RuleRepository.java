package org.example.snippetservice.domains.rule.repository;

import org.example.snippetservice.domains.rule.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, String> {
}