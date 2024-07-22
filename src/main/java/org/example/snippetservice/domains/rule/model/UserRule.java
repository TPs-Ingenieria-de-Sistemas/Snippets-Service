package org.example.snippetservice.domains.rule.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_rule")
public class UserRule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userId;

	@Column(name = "rule_value")
	private String value;

	private boolean isActive;

	@ManyToOne
	@JoinColumn(name = "rule_id", nullable = false)
	private Rule rule;

	// Constructor
	public UserRule(String userId, String value, boolean isActive, Rule rule) {
		this.userId = userId;
		this.value = value;
		this.isActive = isActive;
		this.rule = rule;
	}

	public UserRule() {

	}

	// Getters and setters
	public Long getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}
}
