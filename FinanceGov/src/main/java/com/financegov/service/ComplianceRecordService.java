package com.financegov.service;

import java.util.List;
import java.util.Map;

import com.financegov.dto.ComplianceCreateRequest;
import com.financegov.dto.ComplianceResponse;
import com.financegov.dto.ComplianceUpdateRequest;

public interface ComplianceRecordService {
	List<ComplianceResponse> findAll();

	List<ComplianceResponse> findByEntityId(long entityId);

	Map<String, Integer> getSummary();

	ComplianceResponse findById(long complianceId);

	ComplianceResponse create(ComplianceCreateRequest complianceRecord);

	ComplianceResponse update(long complianceId, ComplianceUpdateRequest complianceRecord);

	String delete(long complianceId);

}