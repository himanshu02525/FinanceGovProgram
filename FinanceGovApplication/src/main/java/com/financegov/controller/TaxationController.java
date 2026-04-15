package com.financegov.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.financegov.dto.TaxRequestDTO;
import com.financegov.dto.TaxResponseDTO;
import com.financegov.enums.TaxStatus;
import com.financegov.model.TaxRecord;
import com.financegov.service.TaxationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/taxation")
@Slf4j
public class TaxationController {

    private final TaxationService taxationService;

    @PostMapping("/enter_taxrecord")
    public ResponseEntity<TaxRecord> createTaxRecord(@Valid @RequestBody TaxRequestDTO request) {
        return ResponseEntity.ok(taxationService.createTaxRecord(request));
    }

    @GetMapping("/taxrecords/{taxId}")
    public ResponseEntity<TaxResponseDTO> getTaxRecordByTaxId(@PathVariable Long taxId) {
        // Fixed: Call getTaxRecordByTaxId to match the Service
        return ResponseEntity.ok(taxationService.getTaxRecordByTaxId(taxId));
    }

    @GetMapping("/admin/all_taxrecords")
    public ResponseEntity<List<TaxRecord>> getAllRecords() {
        return ResponseEntity.ok(taxationService.getAllTaxRecords());
    }

    @PatchMapping("/taxrecords/entity/{entityId}/verify")
    public ResponseEntity<List<TaxRecord>> verifyEntityTaxes(@PathVariable Long entityId, @RequestParam TaxStatus status) {
        return ResponseEntity.ok(taxationService.verifyTaxRecordsByEntity(entityId, status));
    }

    @PatchMapping("/taxrecords/{taxId}/verify")
    public ResponseEntity<TaxRecord> verifySingleTax(@PathVariable Long taxId, @RequestParam TaxStatus status) {
        return ResponseEntity.ok(taxationService.verifySingleTaxRecord(taxId, status));
    }
}