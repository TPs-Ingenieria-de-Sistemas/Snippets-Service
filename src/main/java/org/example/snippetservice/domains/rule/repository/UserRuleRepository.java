package org.example.snippetservice.domains.rule.repository;

import java.util.List;
import org.example.snippetservice.domains.rule.dto.RuleType;
import org.example.snippetservice.domains.rule.model.UserRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRuleRepository extends JpaRepository<UserRule, String> {
	List<UserRule> findAllByUserId(String userId);

	List<UserRule> findAllByUserIdAndRule_RuleType(String userId, RuleType ruleType);
}
