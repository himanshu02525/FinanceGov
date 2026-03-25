package com.financegov.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financegov.model.BudgetAllocation;

public interface BudgetAllocationRepository extends JpaRepository<BudgetAllocation, Long> 
{
	
	// these is dsl 
	List<BudgetAllocation> findByProgramId(Long programId);
}