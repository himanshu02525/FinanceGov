package com.financegov.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.financegov.dto.ComplianceCreateRequest;
import com.financegov.dto.ComplianceResponse;
import com.financegov.dto.ComplianceUpdateRequest;
import com.financegov.enums.ComplianceRecordResult;
import com.financegov.enums.ComplianceRecordType;
import com.financegov.exceptions.AuditStatusConflictException;
import com.financegov.exceptions.ComplianceNotFoundException;
import com.financegov.exceptions.ComplianceStatusConflictException;
import com.financegov.model.ComplianceRecord;
import com.financegov.repository.ComplianceRecordRepository;
import com.financegov.util.MessageUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

	private ComplianceRecordRepository repository;
	private final ModelMapper modelMapper;
	private MessageUtil messageUtil;
	private String compliance = "Compliance";
	private String notFoundMessage = "not.found.message";

	public ComplianceRecordServiceImpl(ComplianceRecordRepository repository, ModelMapper modelMapper,
			MessageUtil messageUtil) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.messageUtil = messageUtil;
	}

	@Override
	public List<ComplianceResponse> findAll() {
		log.info("Fetching all compliance records");

		List<ComplianceResponse> result = repository.findAll().stream()
				.map(c -> modelMapper.map(c, ComplianceResponse.class)).toList();

		log.info("Total compliance records fetched: {}", result.size());
		return result;
	}

	@Override
	public ComplianceResponse findById(long complianceId) {
		log.info("Fetching compliance record with ID: {}", complianceId);

		ComplianceRecord complianceRecord = repository.findById(complianceId).orElseThrow(() -> {
			log.warn("Compliance record not found for ID: {}", complianceId);
			return new ComplianceNotFoundException(messageUtil.getMessage(notFoundMessage, compliance, complianceId));
		});

		log.info("Compliance record found for ID: {}", complianceId);
		return modelMapper.map(complianceRecord, ComplianceResponse.class);
	}

	private Optional<Boolean> getById(long id) {
		return Optional.of(true);
//		return Optional.empty();

	}

	private void validateReference(ComplianceRecordType complianceRecordType, long referenceId) {
		String notFoundKey = "validation.reference.notfound";

		switch (complianceRecordType) {
		case PROGRAM -> getById(referenceId).orElseThrow(() -> new ComplianceNotFoundException(
				messageUtil.getMessage(notFoundKey, ComplianceRecordType.PROGRAM.toString(), referenceId)));

		case SUBSIDY -> getById(referenceId).orElseThrow(() -> new ComplianceNotFoundException(
				messageUtil.getMessage(notFoundKey, ComplianceRecordType.SUBSIDY.toString(), referenceId)));

		case TAX -> getById(referenceId).orElseThrow(() -> new ComplianceNotFoundException(
				messageUtil.getMessage(notFoundKey, ComplianceRecordType.TAX.toString(), referenceId)));

		}
	}

	@Override
	public ComplianceResponse create(ComplianceCreateRequest complianceRecord) {

		log.info("Creating new compliance record");

		ComplianceRecordType recordType = complianceRecord.getType();
		validateReference(recordType, complianceRecord.getReferenceId());
		ComplianceRecord saved = repository.save(modelMapper.map(complianceRecord, ComplianceRecord.class));

		log.info("Compliance record created with ID: {}", saved.getComplianceId());

		return modelMapper.map(saved, ComplianceResponse.class);
	}

	@Override
	public ComplianceResponse update(long complianceId, ComplianceUpdateRequest complianceBody) {
		log.info("Updating compliance record with ID: {}", complianceId);

		ComplianceRecord existingRecord = repository.findById(complianceId)
				.orElseThrow(() -> new ComplianceNotFoundException(
						messageUtil.getMessage(notFoundMessage, compliance, complianceId)));

		if (existingRecord.getResult() == ComplianceRecordResult.PASS
				|| existingRecord.getResult() == ComplianceRecordResult.FAIL) {

			throw new ComplianceStatusConflictException(messageUtil.getMessage("record.update.invalid.message",
					compliance, existingRecord.getResult().toString(), complianceId));
		}
		if (complianceBody.getResult() == ComplianceRecordResult.PENDING) {
			throw new AuditStatusConflictException(messageUtil.getMessage("record.status.pending.invalid", compliance,
					ComplianceRecordResult.PENDING, complianceId));
		}

		if (complianceBody.getResult() == ComplianceRecordResult.PASS
				|| complianceBody.getResult() == ComplianceRecordResult.FAIL) {
			existingRecord.setClosedAt(LocalDateTime.now());
		}

		existingRecord.setNotes(complianceBody.getNotes());
		existingRecord.setResult(complianceBody.getResult());
		ComplianceRecord updated = repository.save(existingRecord);

		log.info("Compliance record updated successfully for ID: {}", complianceId);

		return modelMapper.map(updated, ComplianceResponse.class);
	}

	@Override
	public String delete(long complianceId) {
		log.info("Attempting to delete compliance record with ID: {}", complianceId);

		if (repository.findById(complianceId).isEmpty()) {
			log.warn("Delete failed — compliance ID {} not found", complianceId);
			throw new ComplianceNotFoundException(messageUtil.getMessage(notFoundMessage, compliance, complianceId));
		}
		repository.deleteById(complianceId);
		log.info("Compliance record deleted successfully with ID: {}", complianceId);

		return messageUtil.getMessage("delete.message", "Compliance", complianceId);
	}

	@Override
	public List<ComplianceResponse> findByEntityId(long entityId) {
		log.info("Fetching compliance records for Entity ID: {}", entityId);

		List<ComplianceResponse> result = repository.findByEntityId(entityId).stream()
				.map(complianceRecord -> modelMapper.map(complianceRecord, ComplianceResponse.class)).toList();

		log.info("Total records found for Entity ID {}: {}", entityId, result.size());
		return result;
	}

	@Override
	public Map<String, Integer> getSummary() {
		log.info("Generating compliance summary by result status");

		Map<String, Integer> summary = new LinkedHashMap<>();
		int allCount = 0;
		for (ComplianceRecordResult status : ComplianceRecordResult.values()) {
			int countByResult = repository.countByResult(status);
			allCount += countByResult;
			summary.put(status.toString(), countByResult);
			log.debug("Status: {}, Count: {}", status, countByResult);
		}
		summary.put("All", allCount);
		log.info("Compliance summary generated successfully");
		return summary;
	}

}