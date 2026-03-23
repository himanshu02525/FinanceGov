package com.financegov.model;

import java.time.LocalDate;

import com.financegov.enums.AuditScope;
import com.financegov.enums.AuditStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "Audit")
//@AuditFindingsRequiredIfCompleted
public class Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AuditID", updatable = false, nullable = false)
	private Long auditId;

	@NotNull(message = "{audit.officerId.notNull}")
	@Positive(message = "{audit.officerId.positive}")
	@Column(name = "OfficerID", nullable = false,updatable = false)
	private Long officerId;

	@NotNull(message = "{audit.scope.notNull}")
	@Enumerated(EnumType.STRING)
	@Column(name = "Scope", nullable = false, length = 50)
	private AuditScope scope;

	@Size(max = 2000, message = "{audit.findings.size}")
	@Column(name = "Findings", length = 2000)
	private String findings;

	@NotNull(message = "{audit.date.notNull}")
	@PastOrPresent(message = "{audit.date.pastOrPresent}")
	@Column(name = "Date", nullable = false)
	private LocalDate date;

	@NotNull(message = "{audit.status.notNull}")
	@Column(name = "Status", nullable = false)
	@Enumerated(EnumType.STRING)
	private AuditStatus status;
}