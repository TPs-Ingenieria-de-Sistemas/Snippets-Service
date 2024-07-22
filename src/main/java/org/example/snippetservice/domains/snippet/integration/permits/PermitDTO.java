package org.example.snippetservice.domains.snippet.integration.permits;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PermitDTO {
	@NotNull
	String userId;
	@NotNull
	Integer permissions;
}
