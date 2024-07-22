package org.example.snippetservice.domains.rule.dto;

public class UserRuleDTO {
	private String id;
	private String userId;
	private String name;
	private boolean isActive;
	private String value;
	private RuleType type;
	private RuleValueType valueType;

	// Constructor
	public UserRuleDTO(String id, String userId, String name, boolean isActive, String value, RuleType type,
			RuleValueType valueType) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.isActive = isActive;
		this.value = value;
		this.type = type;
		this.valueType = valueType;
	}

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public RuleType getType() {
		return type;
	}

	public void setType(RuleType type) {
		this.type = type;
	}

	public RuleValueType getValueType() {
		return valueType;
	}

	public void setValueType(RuleValueType valueType) {
		this.valueType = valueType;
	}
}
