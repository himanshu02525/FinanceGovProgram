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

import com.financegov.dto.AuditCreateRequest;
import com.financegov.dto.AuditResponse;
import com.financegov.dto.AuditUpdateRequest;
import com.financegov.service.AuditService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/audit")
public class AuditController {

	private final AuditService service;

	public AuditController(AuditService service) {
		this.service = service;
	}

	@GetMapping
	public List<AuditResponse> getAll() {
		log.info("REST request to get all audit records");
		return service.findAll();
	}

	@GetMapping("/{id}")
	public AuditResponse getById(@PathVariable long id) {
		log.info("REST request to get audit record by ID: {}", id);
		return service.findById(id);
	}

	@GetMapping("/officer/{id}")
	public List<AuditResponse> findByOfficerId(@PathVariable long id) {
		log.info("REST request to find audit records for officer ID: {}", id);
		return service.findByOfficerId(id);
	}

	@PostMapping
	public AuditResponse create(@Valid @RequestBody AuditCreateRequest body) {
		log.info("REST request to create a new audit record");
		return service.create(body);
	}

	@PutMapping("/{id}")
	public AuditResponse update(@PathVariable long id, @RequestBody AuditUpdateRequest body) {
		log.info("REST request to update audit record with ID: {}", id);
		return service.update(id, body);
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable long id) {
		log.warn("REST request to delete audit record with ID: {}", id);
		return service.delete(id);
	}

	@GetMapping("/summary")
	public Map<String, Integer> getSummary() {
		log.info("REST request to fetch audit summary statistics");
		return service.getSummary();
	}
}