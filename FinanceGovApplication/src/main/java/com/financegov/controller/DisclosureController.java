package com.financegov.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.financegov.dto.DisclosureCreateRequestDTO;
import com.financegov.enums.DisclosureStatus;
import com.financegov.model.Disclosure;
import com.financegov.service.DisclosureService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/disclosure")
public class DisclosureController {

    private static final Logger logger = LoggerFactory.getLogger(DisclosureController.class);
    private final DisclosureService disclosureService;

    // Submit new disclosure (Citizen/Business)
    @PostMapping("/enter_disclosure")
    public ResponseEntity<Disclosure> createDisclosure(@Valid @RequestBody DisclosureCreateRequestDTO request) {
        logger.info("REST request to submit disclosure for Entity ID: {}", request.getEntityId());
        return ResponseEntity.ok(disclosureService.processDisclosure(request));
    }

    // Show all disclosures (Admin/Officer access)
    @GetMapping("/all")
    public ResponseEntity<List<Disclosure>> getAllDisclosures() {
        logger.info("REST request to fetch all disclosures");
        return ResponseEntity.ok(disclosureService.getAllDisclosures());
    }

    
    
    // Get a single disclosure by its unique ID
    @GetMapping("/{disclosureId}")
    public ResponseEntity<Disclosure> getDisclosureById(@PathVariable("disclosureId") Long disclosureId) {
        logger.info("REST request to fetch Disclosure ID: {}", disclosureId);
        
        Disclosure disclosure = disclosureService.getDisclosureByDisclosureId(disclosureId);
        return ResponseEntity.ok(disclosure);
    }
    
    
    // Financial Officer validates all disclosures for a specific entity

    @PatchMapping("/{entityId}/validate-disclosure")
       public ResponseEntity<List<Disclosure>> validateEntityDisclosures(
               @PathVariable("entityId") Long entityId,
               @RequestParam("status") DisclosureStatus status) {

           logger.info(
               "REST request: Validating disclosures for Entity ID: {} with status: {}",
               entityId, status
           );

           
           return ResponseEntity.ok(disclosureService.validateDisclosuresByEntity(entityId, status));

          
       }
    
    
 // Financial Officer validates a SINGLE disclosure
    @PatchMapping("/{disclosureId}/validate")
    public ResponseEntity<Disclosure> validateSingleDisclosure(
            @PathVariable("disclosureId") Long disclosureId,
            @RequestParam("status") DisclosureStatus status) {

        logger.info("REST request: Validating Disclosure ID: {} with status: {}", disclosureId, status);
        return ResponseEntity.ok(disclosureService.validateSingleDisclosure(disclosureId, status));
    }

}