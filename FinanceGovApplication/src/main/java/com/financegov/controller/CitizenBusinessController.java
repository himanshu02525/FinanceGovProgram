package com.financegov.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.financegov.dto.CitizenBusinessRequestDTO;
import com.financegov.dto.CitizenBusinessResponseDTO;
import com.financegov.model.CitizenBusiness;
import com.financegov.service.CitizenBusinessService;

import jakarta.validation.Valid;
 
@RestController
@RequestMapping("/entities")
public class CitizenBusinessController {

    private static final Logger logger =
            LoggerFactory.getLogger(CitizenBusinessController.class);

    @Autowired
    private CitizenBusinessService service;

    // Create Citizen / Business
    @PostMapping("/create")
    public ResponseEntity<CitizenBusinessResponseDTO> createCitizen(
            @Valid @RequestBody CitizenBusinessRequestDTO request) {

        return new ResponseEntity<>(
                service.createCitizen(request),
                HttpStatus.CREATED
        );
    }

    // Get all entities
    @GetMapping("/findAll")
    public List<CitizenBusiness> getAllEntities() {
        logger.info("Fetching all entities");
        return service.getAllCitizens();
    }

    // Get entity by ID
    @GetMapping("/find/{id}")
    public CitizenBusiness getCitizenById(@PathVariable Long id) {
        logger.info("Fetching entity with ID: {}", id);
        return service.getCitizenById(id);
    }

    // Update entity
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCitizen(
            @PathVariable Long id,
            @Valid @RequestBody CitizenBusiness citizen) {

        logger.info("Updating entity with ID: {}", id);
        service.updateCitizen(id, citizen);
        return ResponseEntity.ok("Entity updated successfully");
    }

    // Approve entity
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveCitizen(@PathVariable Long id) {
        logger.info("Approving entity with ID: {}", id);
        service.approveCitizen(id);
        return ResponseEntity.ok("Entity approved successfully");
    }

    // Delete entity
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCitizen(@PathVariable Long id) {
        logger.info("Deleting entity with ID: {}", id);
        service.deleteCitizen(id);
        return ResponseEntity.ok("Entity deleted successfully");
    }
}
