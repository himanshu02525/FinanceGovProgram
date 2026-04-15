package com.financegov.service;
import java.util.List;

import com.financegov.dto.EntityDocumentRequestDTO;
import com.financegov.dto.EntityDocumentResponseDTO;
import com.financegov.enums.DocType;
import com.financegov.model.EntityDocument;


public interface EntityDocumentService {
	
	EntityDocumentResponseDTO uploadDocument(Long entityId, EntityDocumentRequestDTO request);
   
   List<EntityDocument> getAllDocuments();
   
   void verifyDocument(Long entityId, DocType docType );
   
   void rejectDocument(Long entityId, DocType docType);
   
   EntityDocumentResponseDTO updateDocument(Long entityId, DocType docType, EntityDocumentRequestDTO request);
   
 
	   
	   
	   
	
}