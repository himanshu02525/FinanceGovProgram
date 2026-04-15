package com.financegov.service;

import java.util.List;

import com.financegov.dto.CitizenBusinessRequestDTO;
import com.financegov.dto.CitizenBusinessResponseDTO;
import com.financegov.model.CitizenBusiness;

public interface CitizenBusinessService {

	CitizenBusinessResponseDTO createCitizen(CitizenBusinessRequestDTO request);

	List<CitizenBusiness> getAllCitizens();

	CitizenBusiness getCitizenById(Long id);

	void deleteCitizen(Long id);

	CitizenBusiness updateCitizen(Long id, CitizenBusiness citizen);

	CitizenBusiness approveCitizen(Long id);
}