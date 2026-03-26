package com.financegov.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financegov.dto.BudgetAllocationRequestDTO;
import com.financegov.dto.BudgetAllocationResponseDTO;
import com.financegov.dto.BudgetSummaryDTO;
import com.financegov.service.BudgetAllocationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budget-allocations")
@RequiredArgsConstructor
public class BudgetAllocationController {

    private static final Logger logger = LoggerFactory.getLogger(BudgetAllocationController.class);
    private final BudgetAllocationService budgetAllocationService;

    @PostMapping("/createAllocation")
    public ResponseEntity<BudgetAllocationResponseDTO> createAllocation(
            @Valid @RequestBody BudgetAllocationRequestDTO dto) {

        logger.info("Received request to create budget allocation for programId: {}", dto.getProgramId());

        ResponseEntity<BudgetAllocationResponseDTO> response =
                new ResponseEntity<>(budgetAllocationService.createAllocation(dto), HttpStatus.CREATED);

        logger.info("Budget allocation created successfully for programId: {}", dto.getProgramId());
        return response;
    }

    @GetMapping("/getAllAllocation")
    public ResponseEntity<List<BudgetAllocationResponseDTO>> getAllAllocations() {
        logger.info("Received request to fetch all budget allocations");

        ResponseEntity<List<BudgetAllocationResponseDTO>> response =
                ResponseEntity.ok(budgetAllocationService.getAllAllocations());

        logger.info("Successfully fetched all budget allocations");
        return response;
    }

    @GetMapping("/summary/{programId}")
    public ResponseEntity<BudgetSummaryDTO> getBudgetSummary(@PathVariable Long programId) {
        logger.info("Received request to get budget summary for programId: {}", programId);

        ResponseEntity<BudgetSummaryDTO> response =
                ResponseEntity.ok(budgetAllocationService.getBudgetSummary(programId));

        logger.info("Budget summary returned successfully for programId: {}", programId);
        return response;
    }

    @DeleteMapping("/deleteAllocation/{allocationId}")
    public ResponseEntity<Map<String, String>> deleteAllocation(@PathVariable Long allocationId) {
        logger.info("Received request to delete budget allocation with allocationId: {}", allocationId);

        Map<String, String> response = new HashMap<>();
        response.put("message", budgetAllocationService.deleteAllocation(allocationId));

        logger.info("Budget allocation deleted successfully with allocationId: {}", allocationId);
        return ResponseEntity.ok(response);
    }
}
