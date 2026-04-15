package com.financegov.service;

import java.time.LocalDate;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.financegov.dto.TaxRequestDTO;
import com.financegov.dto.TaxResponseDTO;
import com.financegov.enums.TaxStatus;
import com.financegov.exceptions.EntityNotFoundException;
import com.financegov.exceptions.ResourceNotFoundException;
import com.financegov.exceptions.InvalidTaxYearException;
import com.financegov.model.CitizenBusiness;
import com.financegov.model.TaxRecord;
import com.financegov.repository.CitizenBusinessRepository;
import com.financegov.repository.TaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor 
public class TaxationServiceImpl implements TaxationService {

    private final TaxRepository taxRepository; 
    private final CitizenBusinessRepository citizenRepository; 

    /**
     * Creates a new tax record with dynamic year validation.
     * Allowed years: Current Year (2026) and Previous Year (2025).
     */
    @Override
    @Transactional
    public TaxRecord createTaxRecord(TaxRequestDTO request) {
        // 1. Verify if the entity exists in the Government Registration system
        CitizenBusiness citizen = citizenRepository.findById(request.getEntityId())
                .orElseThrow(() -> new EntityNotFoundException("Entity ID " + request.getEntityId() + " not found"));

        // 2. DYNAMIC YEAR LOGIC: No manual updates needed on Jan 1st
        int requestedYear = request.getYear();
        int currentYear = LocalDate.now().getYear(); 
        int previousYear = currentYear - 1;          

        // 3. ENFORCE POLICY: Reject any year that isn't current or previous
        if (requestedYear != currentYear && requestedYear != previousYear) {
            log.error("Filing rejected: Year {} is outside allowed window", requestedYear);
            throw new InvalidTaxYearException("Invalid Tax Year: " + requestedYear + 
                ". Allowed: " + currentYear + " or " + previousYear);
        }

        TaxRecord taxRecord = new TaxRecord();
        taxRecord.setCitizenBusiness(citizen); 
        taxRecord.setYear(requestedYear); 
        taxRecord.setAmount(request.getAmount()); 
        taxRecord.setStatus(TaxStatus.PENDING); // Default status for new filings
        
        log.info("Successfully filed tax record for Entity: {} [Year: {}]", request.getEntityId(), requestedYear);
        return taxRepository.save(taxRecord); 
    }

    /**
     * Implementation for finding a single tax record by its ID.
     */
    @Override
    public TaxResponseDTO getTaxRecordByTaxId(Long taxId) {
        log.info("Fetching details for Tax ID: {}", taxId);
        
        // Reuses findById logic for consistency
        return findById(taxId);
    }

    /**
     * BULK VERIFICATION logic used primarily by Compliance or Financial Officers.
     * Updates all tax records for a specific citizen/business in one transaction.
     */
    @Override
    @Transactional
    public List<TaxRecord> verifyTaxRecordsByEntity(Long entityId, TaxStatus newStatus) {
        log.info("Bulk updating status to {} for Entity ID: {}", newStatus, entityId);

        // 1. Fetch all records associated with this specific entity
        List<TaxRecord> records = taxRepository.findByCitizenBusiness_EntityId(entityId);
        
        // 2. If no records exist, throw a clear error for the auditor
        if (records.isEmpty()) {
            throw new ResourceNotFoundException("No tax history found for Entity: " + entityId);
        }

        // 3. Batch update the status for every record in the list
        records.forEach(record -> record.setStatus(newStatus));

        // 4. Save the entire batch back to MySQL
        return taxRepository.saveAll(records);
    }

    /**
     * Core retrieval method with custom error handling.
     */
    @Override
    public TaxResponseDTO findById(Long taxId) {
        TaxRecord record = taxRepository.findById(taxId)
                .orElseThrow(() -> new ResourceNotFoundException("Tax record not found: " + taxId));
        return mapToResponse(record); 
    }

    @Override
    public List<TaxRecord> getAllTaxRecords() {
        return taxRepository.findAll(); 
    }

    @Override
    @Transactional
    public TaxRecord verifySingleTaxRecord(Long taxId, TaxStatus newStatus) {
        TaxRecord record = taxRepository.findById(taxId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found: " + taxId));
        record.setStatus(newStatus);
        return taxRepository.save(record);
    }

    /**
     * Helper: Maps Entity data to a Secure Response DTO.
     */
    private TaxResponseDTO mapToResponse(TaxRecord record) {
        return TaxResponseDTO.builder()
                .taxId(record.getTaxId())
                .entityId(record.getCitizenBusiness().getEntityId())
                .year(record.getYear())
                .amount(record.getAmount())
                .status(record.getStatus())
                .build();
    }
}