package com.financegov.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.financegov.model.Disclosure;

@Repository
public interface DisclosureRepository extends JpaRepository<Disclosure, Long> {

	// FIX: Navigate from Disclosure -> CitizenBusiness -> EntityId
	List<Disclosure> findByCitizenBusiness_EntityId(Long entityId);

	// FIX: Navigate to EntityId and check Type (String-based)
	List<Disclosure> findByCitizenBusiness_EntityIdAndType(Long entityId, String type);
	
	// Count specific types of disclosures for an entity
	@Query("SELECT COUNT(d) FROM Disclosure d WHERE d.citizenBusiness.entityId = :entityId AND d.type = :type")
	Long countByEntityIdAndType(@Param("entityId") Long entityId, @Param("type") com.financegov.enums.DisclosureType type);
}