package org.example.snippetservice.domains.rule.dto;

public class UpdateUserRuleDTO {
	private String id;
	private boolean isActive;
	private String value;

	// Constructor
	public UpdateUserRuleDTO(String id, boolean isActive, String value) {
		this.id = id;
		this.isActive = isActive;
		this.value = value;
	}

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
