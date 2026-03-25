
package com.financegov.serviceImpl;

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
import com.financegov.service.BudgetAllocationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetAllocationServiceImpl implements BudgetAllocationService {

	private static final Logger logger = LoggerFactory.getLogger(BudgetAllocationServiceImpl.class);
	private final BudgetAllocationRepository budgetAllocationRepository;

	private AllocationStatus mapStatus(String status) {
		try {
			return AllocationStatus.valueOf(status.toUpperCase());
		} catch (Exception ex) {
			throw new InvalidAllocationStatusException("Status must be either ALLOCATED or CANCELLED");
		}
	}

	@Override
	public BudgetAllocationResponseDTO createAllocation(BudgetAllocationRequestDTO dto) {
		logger.info("Allocating budget to program {}", dto.getProgramId());

		BudgetAllocation allocation = BudgetAllocation.builder().programId(dto.getProgramId()).amount(dto.getAmount())
				.date(dto.getDate()).status(mapStatus(dto.getStatus())).build();

		BudgetAllocation saved = budgetAllocationRepository.save(allocation);

		return BudgetAllocationResponseDTO.builder().allocationId(saved.getAllocationId())
				.programId(saved.getProgramId()).amount(saved.getAmount()).date(saved.getDate())
				.status(saved.getStatus().name()).build();
	}

	@Override
	public List<BudgetAllocationResponseDTO> getAllAllocations() {
		List<BudgetAllocationResponseDTO> responseList = new ArrayList<>();

		for (BudgetAllocation allocation : budgetAllocationRepository.findAll()) {
			responseList.add(BudgetAllocationResponseDTO.builder().allocationId(allocation.getAllocationId())
					.programId(allocation.getProgramId()).amount(allocation.getAmount()).date(allocation.getDate())
					.status(allocation.getStatus().name()).build());
		}
		return responseList;
	}

	@Override
	public BudgetSummaryDTO getBudgetSummary(Long programId) {

		BigDecimal totalAllocated = BigDecimal.ZERO;

		// Total budget allocated by Program Manager for this program
		for (BudgetAllocation allocation : budgetAllocationRepository.findByProgramId(programId)) {
			totalAllocated = totalAllocated.add(allocation.getAmount());
		}

		// Once Module3 is implemented, this value will be
		// SUM(of approved subsidy amounts for this program)

		BigDecimal totalUsed = totalAllocated.multiply(new BigDecimal("0.30"));

		// Remaining budget
		BigDecimal remaining = totalAllocated.subtract(totalUsed);

		// these will be used when 3rd module will be integrated
		// ACTUAL used amount from Module3 (approved subsidies)
		
		// BigDecimal totalUsed = subsidyRepository.sumApprovedAmountByProgramId(programId);
        // if (totalUsed == null) {
		// totalUsed = BigDecimal.ZERO;
	    // }


		return BudgetSummaryDTO.builder().programId(programId).totalAllocated(totalAllocated).totalUsed(totalUsed)
				.remaining(remaining).build();
	}

	@Override
	public String deleteAllocation(Long allocationId) {
		BudgetAllocation allocation = budgetAllocationRepository.findById(allocationId)
				.orElseThrow(() -> new AllocationNotFoundException("Budget allocation not found"));

		budgetAllocationRepository.delete(allocation);
		return "Budget allocation deleted successfully";
	}
}
