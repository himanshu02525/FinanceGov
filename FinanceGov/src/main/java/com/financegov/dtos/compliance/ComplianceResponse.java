package com.financegov.dtos.compliance;

import java.time.LocalDateTime;

import com.financegov.enums.ComplianceRecordResult;
import com.financegov.enums.ComplianceRecordType;

import lombok.Data;

@Data
public class ComplianceResponse {

	private Long complianceId;
	private Long referenceId;
	private Long entityId;

	private ComplianceRecordType type;

	private ComplianceRecordResult result;

	private LocalDateTime createdAt;

	private LocalDateTime closedAt;

	private String notes;
}
