package com.financegov.repository;
 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financegov.enums.DocType;
import com.financegov.model.CitizenBusiness;
import com.financegov.model.EntityDocument;
                                                                                                                                                                                                                                                                                                                                              
public interface EntityDocumentRepository extends JpaRepository<EntityDocument, Long> {
	  
	   Optional<EntityDocument> findByCitizenBusinessAndDocType(CitizenBusiness citizenBusiness,DocType docType);
	}