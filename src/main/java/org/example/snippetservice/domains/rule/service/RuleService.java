package org.example.snippetservice.domains.rule.service;

import java.util.List;
import org.example.snippetservice.domains.rule.dto.RuleType;
import org.example.snippetservice.domains.rule.dto.UpdateUserRuleDTO;
import org.example.snippetservice.domains.rule.dto.UserRuleDTO;

public interface RuleService {
	List<UserRuleDTO> getRulesForUserByType(String userId, RuleType ruleType);

	List<UserRuleDTO> updateUserRules(String userId, List<UpdateUserRuleDTO> updatedRules);
}
