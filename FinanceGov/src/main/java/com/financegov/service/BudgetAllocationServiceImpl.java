package com.financegov.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.financegov.dto.BudgetAllocationRequestDTO;
import com.financegov.dto.BudgetAllocationResponseDTO;
import com.financegov.dto.BudgetSummaryDTO;
import com.financegov.enums.AllocationStatus;
import com.financegov.exceptions.AllocationNotFoundException;
import com.financegov.exceptions.InvalidAllocationStatusException;
import com.financegov.model.BudgetAllocation;
import com.financegov.repository.BudgetAllocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetAllocationServiceImpl implements BudgetAllocationService {

	private static final Logger logger = LoggerFactory.getLogger(BudgetAllocationServiceImpl.class);
	private final BudgetAllocationRepository budgetAllocationRepository;

	private AllocationStatus mapStatus(String status) {
		logger.debug("Mapping allocation status: {}", status);
		try {
			return AllocationStatus.valueOf(status.toUpperCase());
		} catch (Exception ex) {
			logger.error("Invalid allocation status received: {}", status);
			throw new InvalidAllocationStatusException("Status must be either ALLOCATED or CANCELLED");
		}
	}

	@Override
	public BudgetAllocationResponseDTO createAllocation(BudgetAllocationRequestDTO dto) {
		logger.info("Creating budget allocation for programId: {}", dto.getProgramId());

		BudgetAllocation allocation = BudgetAllocation.builder().programId(dto.getProgramId()).amount(dto.getAmount())
				.date(dto.getDate()).status(mapStatus(dto.getStatus())).build();

		BudgetAllocation saved = budgetAllocationRepository.save(allocation);

		logger.info("Budget allocation created successfully with allocationId: {}", saved.getAllocationId());

		return BudgetAllocationResponseDTO.builder().allocationId(saved.getAllocationId())
				.programId(saved.getProgramId()).amount(saved.getAmount()).date(saved.getDate())
				.status(saved.getStatus().name()).build();
	}

	@Override
	public List<BudgetAllocationResponseDTO> getAllAllocations() {
		logger.info("Fetching all budget allocations");

		List<BudgetAllocationResponseDTO> responseList = new ArrayList<>();

		for (BudgetAllocation allocation : budgetAllocationRepository.findAll()) {
			logger.debug("Processing allocationId: {}", allocation.getAllocationId());

			responseList.add(BudgetAllocationResponseDTO.builder().allocationId(allocation.getAllocationId())
					.programId(allocation.getProgramId()).amount(allocation.getAmount()).date(allocation.getDate())
					.status(allocation.getStatus().name()).build());
		}

		logger.info("Total allocations fetched: {}", responseList.size());
		return responseList;
	}

	@Override
	public BudgetSummaryDTO getBudgetSummary(Long programId) {
		logger.info("Calculating budget summary for programId: {}", programId);

		BigDecimal totalAllocated = BigDecimal.ZERO;

		for (BudgetAllocation allocation : budgetAllocationRepository.findByProgramId(programId)) {
			logger.debug("Adding allocation amount {} for allocationId {}", allocation.getAmount(),
					allocation.getAllocationId());
			totalAllocated = totalAllocated.add(allocation.getAmount());
		}

		BigDecimal totalUsed = totalAllocated.multiply(new BigDecimal("0.30"));
		BigDecimal remaining = totalAllocated.subtract(totalUsed);

		logger.info("Budget summary calculated for programId: {} | Allocated: {}, Used: {}, Remaining: {}", programId,
				totalAllocated, totalUsed, remaining);

		return BudgetSummaryDTO.builder().programId(programId).totalAllocated(totalAllocated).totalUsed(totalUsed)
				.remaining(remaining).build();
	}

	@Override
	public String deleteAllocation(Long allocationId) {
		logger.info("Deleting budget allocation with allocationId: {}", allocationId);

		BudgetAllocation allocation = budgetAllocationRepository.findById(allocationId).orElseThrow(() -> {
			logger.error("Budget allocation not found for allocationId: {}", allocationId);
			return new AllocationNotFoundException("Budget allocation not found");
		});

		budgetAllocationRepository.delete(allocation);

		logger.info("Budget allocation deleted successfully for allocationId: {}", allocationId);
		return "Budget allocation deleted successfully";
	}
}