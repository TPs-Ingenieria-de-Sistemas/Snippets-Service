package org.example.snippetservice.domains.rule.model;

import jakarta.persistence.*;
import org.example.snippetservice.domains.rule.dto.RuleType;
import org.example.snippetservice.domains.rule.dto.RuleValueType;

@Entity
@Table(name = "rule")
public class Rule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String defaultValue;

	@Enumerated(EnumType.STRING)
	private RuleValueType valueType;

	@Enumerated(EnumType.STRING)
	private RuleType ruleType;

	// Constructor
	public Rule(String name, String defaultValue, RuleValueType valueType, RuleType ruleType) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.valueType = valueType;
		this.ruleType = ruleType;
	}

	public Rule() {

	}

	// Getters and setters
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public RuleValueType getValueType() {
		return valueType;
	}

	public void setValueType(RuleValueType valueType) {
		this.valueType = valueType;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}
}