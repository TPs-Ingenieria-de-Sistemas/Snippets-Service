package org.example.snippetservice.domains.rule.service;

import java.util.ArrayList;
import java.util.List;
import org.example.snippetservice.domains.rule.dto.RuleType;
import org.example.snippetservice.domains.rule.dto.UpdateUserRuleDTO;
import org.example.snippetservice.domains.rule.dto.UserRuleDTO;
import org.example.snippetservice.domains.rule.model.UserRule;
import org.example.snippetservice.domains.rule.repository.UserRuleRepository;
import org.springframework.stereotype.Service;

@Service
public class RuleServiceImpl implements RuleService {

	private final UserRuleRepository userRuleRepository;

	public RuleServiceImpl(UserRuleRepository userRuleRepository) {
		this.userRuleRepository = userRuleRepository;
	}

	@Override
	public List<UserRuleDTO> getRulesForUserByType(String userId, RuleType ruleType) {
		List<UserRule> userRule = this.userRuleRepository.findAllByUserIdAndRule_RuleType(userId, ruleType);
		List<UserRuleDTO> result = new ArrayList<>();

		for (UserRule rule : userRule) {
			if (rule.getIsActive()) {
				result.add(new UserRuleDTO(rule.getId().toString(), rule.getUserId(), rule.getRule().getName(),
						rule.getIsActive(), rule.getValue(), rule.getRule().getRuleType(),
						rule.getRule().getValueType()));
			} else {
				result.add(new UserRuleDTO(rule.getId().toString(), rule.getUserId(), rule.getRule().getName(),
						rule.getIsActive(), rule.getRule().getDefaultValue(), rule.getRule().getRuleType(),
						rule.getRule().getValueType()));
			}
		}
		return result;
	}

	@Override
	public List<UserRuleDTO> updateUserRules(String userId, List<UpdateUserRuleDTO> updatedRules) {
		List<UserRuleDTO> result = new ArrayList<>();
		for (UpdateUserRuleDTO rule : updatedRules) {
			UserRule userRule = this.userRuleRepository.findById(rule.getId()).orElse(null);
			if (userRule != null) {
				userRule.setValue(rule.getValue());
				this.userRuleRepository.save(userRule);
				result.add(new UserRuleDTO(userRule.getId().toString(), userRule.getUserId(),
						userRule.getRule().getName(), userRule.getIsActive(), userRule.getValue(),
						userRule.getRule().getRuleType(), userRule.getRule().getValueType()));
			}
		}
		return result;
	}
}
