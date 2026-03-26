package com.financegov.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.financegov.dto.AuditCreateRequest;
import com.financegov.dto.AuditResponse;
import com.financegov.dto.AuditUpdateRequest;
import com.financegov.enums.AuditStatus;
import com.financegov.exceptions.AuditRecordNotFoundException;
import com.financegov.exceptions.AuditStatusConflictException;
import com.financegov.model.Audit;
import com.financegov.repository.AuditRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final AuditRepository repository;

    private final String audit = "Audit";
    private final String notFoundMessage = "not.found.message";

    public AuditServiceImpl(AuditRepository repository, ModelMapper modelMapper, MessageSource messageSource) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }

    @Override
    public List<AuditResponse> findAll() {
        log.info("Fetching all audit records");

        List<AuditResponse> result = repository.findAll().stream()
                .map(auditRecord -> modelMapper.map(auditRecord, AuditResponse.class))
                .toList();

        log.info("Total audit records fetched: {}", result.size());
        return result;
    }

    @Override
    public Map<String, Integer> getSummary() {
        log.info("Generating audit summary by status");

        Map<String, Integer> summary = new LinkedHashMap<>();
        int allCount = 0;

        for (AuditStatus status : AuditStatus.values()) {
            int countByStatus = repository.countByStatus(status);
            allCount += countByStatus;
            summary.put(status.toString(), countByStatus);
        }

        summary.put("All", allCount);

        log.info("Audit summary generated successfully");
        return summary;
    }

    @Override
    public AuditResponse findById(long auditId) {
        log.info("Fetching audit record with ID: {}", auditId);

        Audit existingAudit = repository.findById(auditId).orElseThrow(() -> {
            log.warn("Audit record not found for ID: {}", auditId);
            return new AuditRecordNotFoundException(getMessage(notFoundMessage, audit, auditId));
        });

        log.info("Audit record found for ID: {}", auditId);
        return modelMapper.map(existingAudit, AuditResponse.class);
    }

    @Override
    public List<AuditResponse> findByOfficerId(long auditId) {
        log.info("Fetching audit records for Officer ID: {}", auditId);

        List<AuditResponse> result = repository.findByOfficerId(auditId).stream()
                .map(auditRecord -> modelMapper.map(auditRecord, AuditResponse.class))
                .toList();

        log.info("Total records found for Officer ID {}: {}", auditId, result.size());
        return result;
    }

    @Override
    public AuditResponse create(AuditCreateRequest auditRecord) {
        log.info("Creating new audit record");

        Audit savedAudit = repository.save(modelMapper.map(auditRecord, Audit.class));

        log.info("Audit record created with ID: {}", savedAudit.getAuditId());
        return modelMapper.map(savedAudit, AuditResponse.class);
    }

    @Override
    public AuditResponse update(long auditId, AuditUpdateRequest auditBody) {

        log.info("Updating audit record with ID: {}", auditId);

        Audit existingAudit = repository.findById(auditId)
                .orElseThrow(() -> new AuditRecordNotFoundException(getMessage(notFoundMessage, audit, auditId)));

        if (existingAudit.getStatus() == AuditStatus.COMPLETED) {
            throw new AuditStatusConflictException(
                    getMessage("record.update.invalid.message", audit, AuditStatus.COMPLETED.toString(), auditId));
        }

        if (auditBody.getStatus() == AuditStatus.PENDING) {
            throw new AuditStatusConflictException(
                    getMessage("record.status.pending.invalid", audit, AuditStatus.PENDING.toString(), auditId));
        }

        existingAudit.setFindings(auditBody.getFindings());
        existingAudit.setStatus(auditBody.getStatus());

        if (auditBody.getStatus() == AuditStatus.COMPLETED || auditBody.getStatus() == AuditStatus.CANCELLED) {
            existingAudit.setClosedAt(LocalDateTime.now());
        }

        Audit updated = repository.save(existingAudit);

        log.info("Audit record updated successfully for ID: {}", auditId);

        return modelMapper.map(updated, AuditResponse.class);
    }

    @Override
    public String delete(long auditId) {
        log.info("Attempting to delete audit record with ID: {}", auditId);

        findById(auditId);
        repository.deleteById(auditId);

        log.info("Audit record deleted successfully with ID: {}", auditId);
        return getMessage("delete.message", audit, auditId);
    }
}