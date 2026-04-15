package com.financegov.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financegov.dto.DisclosureCreateRequestDTO;
import com.financegov.enums.DisclosureStatus;
import com.financegov.exceptions.EntityNotFoundException;
import com.financegov.exceptions.ResourceNotFoundException;
import com.financegov.model.CitizenBusiness;
import com.financegov.model.Disclosure;
import com.financegov.repository.CitizenBusinessRepository;
import com.financegov.repository.DisclosureRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisclosureServiceImpl implements DisclosureService {
  private final DisclosureRepository disclosureRepository;
    
    private final CitizenBusinessRepository citizenRepository;

    @Override
    @Transactional
    public Disclosure processDisclosure(DisclosureCreateRequestDTO request) {
        log.info("Filing disclosure for Entity ID: {}", request.getEntityId());

        CitizenBusiness citizen = citizenRepository.findById(request.getEntityId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Disclosure Filing Failed: No Citizen or Business found with ID: " + request.getEntityId()));

        Disclosure disclosure = new Disclosure();
        disclosure.setCitizenBusiness(citizen);
        disclosure.setType(request.getType());
        disclosure.setStatus(DisclosureStatus.SUBMITTED);
        disclosure.setSubmissionDate(LocalDateTime.now());

        return disclosureRepository.save(disclosure);
    }

    @Override
    public List<Disclosure> getAllDisclosures() {
        return disclosureRepository.findAll();
    }

    
//     IMPLEMENTATION FOR THE SPECIFIC LOOKUP
     
    @Override
    public Disclosure getDisclosureByDisclosureId(Long disclosureId) {
        log.info("Fetching specific disclosure record: {}", disclosureId);
        return disclosureRepository.findById(disclosureId)
                .orElseThrow(() -> new ResourceNotFoundException("Disclosure Record not found with ID: " + disclosureId));
    }

    @Override
    @Transactional
    public List<Disclosure> validateDisclosuresByEntity(Long entityId, DisclosureStatus newStatus) {
        log.info("Bulk validating disclosures for Entity ID: {}", entityId);
        List<Disclosure> disclosures = disclosureRepository.findByCitizenBusiness_EntityId(entityId);

        if (disclosures.isEmpty()) {
            throw new ResourceNotFoundException("No disclosures found for Entity ID: " + entityId);
        }

        disclosures.forEach(d -> {
            if (d.getStatus() == DisclosureStatus.SUBMITTED) {
                d.setStatus(newStatus);
            }
        });
        return disclosureRepository.saveAll(disclosures);
    }

    @Override
    @Transactional
    public Disclosure validateSingleDisclosure(Long disclosureId, DisclosureStatus newStatus) {
        log.info("Validating single disclosure: {}", disclosureId);
        Disclosure disclosure = disclosureRepository.findById(disclosureId)
                .orElseThrow(() -> new ResourceNotFoundException("Disclosure not found with ID: " + disclosureId));

        disclosure.setStatus(newStatus);
        return disclosureRepository.save(disclosure);
    }
}