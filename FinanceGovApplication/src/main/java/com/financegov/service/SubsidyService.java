package com.financegov.service;

import java.util.List;

import com.financegov.dto.SubsidyRequest;
import com.financegov.dto.SubsidyResponse;

public interface SubsidyService {
    SubsidyResponse saveSubsidy(SubsidyRequest request);
    List<SubsidyResponse> getAllSubsidies();
    List<SubsidyResponse> getSubsidiesByProgram(Long programId);
    List<SubsidyResponse> getSubsidiesByEntity(Long entityId);
//    SubsidyResponse updateSubsidyStatus(Long subsidyId, String status);
    SubsidyResponse getSubsidyById(Long subsidyId);
}
