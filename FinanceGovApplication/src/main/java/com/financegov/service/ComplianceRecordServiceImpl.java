package com.financegov.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.financegov.dto.AuditCreateRequest;
import com.financegov.dto.ComplianceCreateRequest;
import com.financegov.dto.ComplianceResponse;
import com.financegov.dto.ComplianceUpdateRequest;
import com.financegov.dto.FinancialProgramResponse;
import com.financegov.dto.SubsidyResponse;
import com.financegov.dto.TaxRequestDTO;
import com.financegov.enums.AuditScope;
import com.financegov.enums.ComplianceRecordResult;
import com.financegov.enums.ComplianceRecordType;
import com.financegov.exceptions.AuditStatusConflictException;
import com.financegov.exceptions.ComplianceNotFoundException;
import com.financegov.exceptions.ComplianceStatusConflictException;
import com.financegov.model.ComplianceRecord;
import com.financegov.repository.ComplianceRecordRepository;
import com.financegov.util.MessageUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

	private final String COMPLIANCE = "Compliance";
	private final String NOT_FOUND_MESSAGE = "not.found.message";

	private final ComplianceRecordRepository repository;
	private final ModelMapper modelMapper;
	private final MessageUtil messageUtil;
	private final TaxationService taxationService;
	private final FinancialProgramService financialProgramService;
	private final SubsidyService subsidyService;
	private final AuditService auditService;

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

		ComplianceRecord complianceRecord = repository.findById(complianceId)
				.orElseThrow(() -> new ComplianceNotFoundException("Not found"));

		ComplianceResponse response = modelMapper.map(complianceRecord, ComplianceResponse.class);

		Long ref = complianceRecord.getReferenceID();

		switch (complianceRecord.getType()) {

		case TAX:
			response.setTax(modelMapper.map(taxationService.findById(ref), TaxRequestDTO.class));
			break;

		case SUBSIDY:
			response.setSubsidyResponse(modelMapper.map(subsidyService.getSubsidyById(ref), SubsidyResponse.class));
			break;

		case PROGRAM:
			response.setFinancialProgramResponse(
					modelMapper.map(financialProgramService.getProgramById(ref), FinancialProgramResponse.class));
			break;
		}

		return response;
	}

	private void validateReference(ComplianceRecordType complianceRecordType, long referenceId) {

		switch (complianceRecordType) {

		case PROGRAM -> financialProgramService.getProgramById(referenceId);
		case SUBSIDY -> subsidyService.getSubsidyById(referenceId);
		case TAX -> taxationService.findById(referenceId);
		default -> throw new IllegalArgumentException("Unexpected value: " + complianceRecordType);
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
						messageUtil.getMessage(NOT_FOUND_MESSAGE, COMPLIANCE, complianceId)));
		if (ComplianceRecordResult.FAIL == complianceBody.getResult()) {
			AuditCreateRequest auditCreateRequest = new AuditCreateRequest();
			auditCreateRequest.setFindings(complianceBody.getNotes());
			auditCreateRequest.setOfficerId((long) 1234);
			auditCreateRequest.setScope(AuditScope.PROGRAM);

			auditService.create(auditCreateRequest);
		}
		if (existingRecord.getResult() == ComplianceRecordResult.PASS
				|| existingRecord.getResult() == ComplianceRecordResult.FAIL) {

			throw new ComplianceStatusConflictException(messageUtil.getMessage("record.update.invalid.message",
					COMPLIANCE, existingRecord.getResult().toString(), complianceId));
		}
		if (complianceBody.getResult() == ComplianceRecordResult.PENDING) {
			throw new AuditStatusConflictException(messageUtil.getMessage("record.status.pending.invalid", COMPLIANCE,
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
			throw new ComplianceNotFoundException(messageUtil.getMessage(NOT_FOUND_MESSAGE, COMPLIANCE, complianceId));
		}
		repository.deleteById(complianceId);
		log.info("Compliance record deleted successfully with ID: {}", complianceId);

		return messageUtil.getMessage("delete.message", "Compliance", complianceId);
	}

	@Override
	public List<ComplianceResponse> findByEntityId(long entityId) {
		log.info("Fetching compliance records for Entity ID: {}", entityId);

		List<ComplianceRecord> complianceRecord = repository.findByEntityId(entityId);

		if (complianceRecord.isEmpty()) {
			throw new ComplianceNotFoundException("No compliance records found for Entity Id : " + entityId);
		}

		log.info("Total records found for Entity ID {}: {}", entityId, complianceRecord.size());

		return complianceRecord.stream().map(audit -> modelMapper.map(audit, ComplianceResponse.class)).toList();
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