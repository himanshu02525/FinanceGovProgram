package com.financegov.service;

import java.util.List;
import com.financegov.dto.DisclosureCreateRequestDTO;
import com.financegov.enums.DisclosureStatus;
import com.financegov.model.Disclosure;

public interface DisclosureService {
    // For Citizens: Submit a new report
    Disclosure processDisclosure(DisclosureCreateRequestDTO request);
    
    // For Officers: View all reports
    List<Disclosure> getAllDisclosures();
    
    // For Officers: View specific report by ID
    Disclosure getDisclosureByDisclosureId(Long disclosureId);
    
    // For Financial Officers: Bulk validate an entity's compliance
    List<Disclosure> validateDisclosuresByEntity(Long entityId, DisclosureStatus newStatus);
    
    // For Financial Officers: Validate one specific record
    Disclosure validateSingleDisclosure(Long disclosureId, DisclosureStatus newStatus);
}