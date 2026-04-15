package com.financegov.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financegov.dto.ComplianceCreateRequest;
import com.financegov.dto.ComplianceResponse;
import com.financegov.dto.ComplianceUpdateRequest;
import com.financegov.service.ComplianceRecordService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/compliance")
public class ComplianceController {

	private final ComplianceRecordService service;

	public ComplianceController(ComplianceRecordService service) {
		this.service = service;
	}

	@GetMapping
	public List<ComplianceResponse> getAll() {
		log.info("REST request to get all compliance records")   ;
		return service.findAll();
	}

	@GetMapping("/{id}")
	public ComplianceResponse getById(@PathVariable long id) {
		log.info("REST request to get compliance record by ID: {}", id);
		return service.findById(id);
	}

	@GetMapping("/entity/{entityId}")
	public List<ComplianceResponse> findByEntityId(@PathVariable long entityId) {
		log.info("REST request to find compliance records for entity ID: {}", entityId);
		return service.findByEntityId(entityId);
	}

	@GetMapping("/summary")
	public Map<String, Integer> getSummary() {
		log.info("REST request to fetch compliance summary statistics");
		return service.getSummary();
	}

	@PostMapping
	public ComplianceResponse create(@Valid @RequestBody ComplianceCreateRequest body) {
		log.info("REST request to create a new compliance record");
		return service.create(body);
	}

	@PutMapping("/{id}")
	public ComplianceResponse update(@PathVariable long id, @RequestBody ComplianceUpdateRequest body) {
		log.info("REST request to update compliance record with ID: {}", id);
		return service.update(id, body);
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable long id) {
		log.warn("REST request to delete compliance record with ID: {}", id);
		return service.delete(id);
	}
}