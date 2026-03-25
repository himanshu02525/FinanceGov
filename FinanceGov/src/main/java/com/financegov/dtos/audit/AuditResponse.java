package com.financegov.dtos.audit;

import java.time.LocalDateTime;

import com.financegov.enums.AuditScope;
import com.financegov.enums.AuditStatus;

import lombok.Data;

@Data
public class AuditResponse {
	private Long auditId;
	private Long officerId;
	private AuditScope scope;
	private String findings;
	private AuditStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime closedAt;

}
