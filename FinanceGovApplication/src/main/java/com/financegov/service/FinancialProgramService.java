package com.financegov.service;

import java.util.List;

import com.financegov.dto.FinancialProgramRequest;
import com.financegov.dto.FinancialProgramResponse;
import com.financegov.enums.ProgramStatus;

public interface FinancialProgramService {
	FinancialProgramResponse saveProgram(FinancialProgramRequest request);
    FinancialProgramResponse updateProgram(Long id, FinancialProgramRequest request);
    String deleteProgram(Long programId);
    FinancialProgramResponse getProgramById(Long programId);
    List<FinancialProgramResponse> getAllPrograms();
    List<FinancialProgramResponse> getProgramsByStatus(ProgramStatus status);
}
