package com.financegov.service;

import java.util.List;
import com.financegov.dto.TaxRequestDTO;
import com.financegov.dto.TaxResponseDTO;
import com.financegov.enums.TaxStatus;
import com.financegov.model.TaxRecord;

public interface TaxationService {
    TaxRecord createTaxRecord(TaxRequestDTO request); // Creates a new tax record
    List<TaxRecord> getAllTaxRecords(); // Fetches all tax records for audit
    
    // This specific method name is required by ComplianceRecordServiceImpl to resolve the mapping error
    TaxResponseDTO findById(Long taxId); 

    TaxResponseDTO getTaxRecordByTaxId(Long taxId); // Retrieves specific record details
    List<TaxRecord> verifyTaxRecordsByEntity(Long entityId, TaxStatus newStatus); // Bulk updates by entity
    TaxRecord verifySingleTaxRecord(Long taxId, TaxStatus newStatus); // Updates status for one record
}