package com.financegov.dtos.audit;

import com.financegov.enums.AuditScope;
import com.financegov.enums.AuditStatus;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class AuditCreateRequest {

	@NotNull(message = "{audit.officerId.notNull}")
	@Positive(message = "{audit.officerId.positive}")
	private Long officerId;

	@NotNull(message = "{audit.scope.notNull}")
	private AuditScope scope;

	@Size(max = 2000, message = "{audit.findings.size}")
	private String findings;

	@NotNull(message = "{audit.status.notNull}")
	private AuditStatus status;
}