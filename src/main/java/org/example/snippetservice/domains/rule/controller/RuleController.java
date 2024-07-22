package org.example.snippetservice.domains.rule.controller;

import java.util.List;
import org.example.snippetservice.domains.rule.dto.RuleType;
import org.example.snippetservice.domains.rule.dto.UpdateUserRuleDTO;
import org.example.snippetservice.domains.rule.dto.UserRuleDTO;
import org.example.snippetservice.domains.rule.service.RuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rules")
public class RuleController {
	private final RuleService ruleService;

	public RuleController(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	@GetMapping("/user-rules")
	public List<UserRuleDTO> getUserRules(Jwt jwt, @RequestParam("ruleType") RuleType ruleType) {
		String userId = jwt.getSubject();
		return ruleService.getRulesForUserByType(userId, ruleType);
	}

	@PutMapping("/update-user-rules")
	public ResponseEntity<List<UserRuleDTO>> updateUserRule(Jwt jwt,
			@RequestBody List<UpdateUserRuleDTO> updatedRules) {
		String userId = jwt.getSubject();
		List<UserRuleDTO> updatedUserRules = ruleService.updateUserRules(userId, updatedRules);
		return ResponseEntity.ok(updatedUserRules);
	}
}
