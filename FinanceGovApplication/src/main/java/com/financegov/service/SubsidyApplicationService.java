package com.financegov.service;

import java.util.List;

import com.financegov.dto.SubsidyApplicationRequest;
import com.financegov.dto.SubsidyApplicationResponse;

public interface SubsidyApplicationService {
    SubsidyApplicationResponse saveApplication(SubsidyApplicationRequest request);
//    List<SubsidyApplicationResponse> getApplicationsByProgram(Long programId);
    SubsidyApplicationResponse approveApplication(Long applicationId);
    SubsidyApplicationResponse rejectApplication(Long applicationId);
//    List<SubsidyApplicationResponse> getApplicationsByEntity(Long entityId);
	List<SubsidyApplicationResponse> getApplicationsByEntity(Long entityId);
}


