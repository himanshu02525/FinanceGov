package com.financegov.model;

import java.time.LocalDate;

import com.financegov.enums.ComplianceRecordResult;
import com.financegov.enums.ComplianceRecordType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class ComplianceRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ComplianceID", nullable = false, updatable = false)
	private Long complianceId;

	@NotNull(message = "{compliance.entityId.notNull}")
	@Positive(message = "{compliance.entityId.positive}")
	@Column(name = "EntityID", nullable = false, updatable = false)
	
	private Long entityId;

	@NotNull(message = "{compliance.type.notNull}")
	@Enumerated(EnumType.STRING)
	@Column(name = "Type", nullable = false, length = 20)
	private ComplianceRecordType type;

	@NotNull(message = "{compliance.result.notNull}")
	@Enumerated(EnumType.STRING)
	@Column(name = "Result", nullable = false, length = 20)
	private ComplianceRecordResult result;

	@NotNull(message = "{compliance.date.notNull}")
	@PastOrPresent(message = "{compliance.date.pastOrPresent}")
	@Column(name = "Date", nullable = false)
	private LocalDate date;

	@Size(max = 1000, message = "{compliance.notes.size}")
	@Column(name = "Notes", length = 1000)
	private String notes;
}