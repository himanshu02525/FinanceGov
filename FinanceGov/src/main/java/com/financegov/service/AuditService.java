package com.financegov.service;

import java.util.List;
import java.util.Map;

import com.financegov.dtos.audit.AuditCreateRequest;
import com.financegov.dtos.audit.AuditResponse;
import com.financegov.dtos.audit.AuditUpdateRequest;

import jakarta.validation.Valid;

public interface AuditService {

	List<AuditResponse> findAll();

	Map<String, Integer> getSummary();

	AuditResponse findById(long id);

	AuditResponse create(@Valid AuditCreateRequest body);

	AuditResponse update(long auditId, AuditUpdateRequest auditRecord);

	String delete(long auditId);

	List<AuditResponse> findByOfficerId(long auditId);
}