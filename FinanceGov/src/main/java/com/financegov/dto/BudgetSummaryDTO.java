package com.financegov.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BudgetSummaryDTO {
	private Long programId;
	private BigDecimal totalAllocated;
	private BigDecimal totalUsed;
	private BigDecimal remaining;
}