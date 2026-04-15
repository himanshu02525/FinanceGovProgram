package com.financegov.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import com.financegov.model.TaxRecord;

@Repository
public interface TaxRepository extends JpaRepository<TaxRecord, Long> {

	// 1. Find by the ID inside the joined CitizenBusiness object
	List<TaxRecord> findByCitizenBusiness_EntityId(Long entityId);

	// 2. Find by Year (This remains the same as it's a direct field)
	List<TaxRecord> findBytaxId(Long entityId);

	// 3. Find by both the joined Entity ID and the Year
	List<TaxRecord> findByCitizenBusiness_EntityIdAndYear(Long entityId, Integer year);


	
	
}