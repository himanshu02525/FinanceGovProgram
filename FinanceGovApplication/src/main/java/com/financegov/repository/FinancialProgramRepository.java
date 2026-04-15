package com.financegov.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.financegov.model.FinancialProgram;
import com.financegov.enums.ProgramStatus;

@Repository
public interface FinancialProgramRepository extends JpaRepository<FinancialProgram, Long> {
    List<FinancialProgram> getProgramsByStatus(ProgramStatus status);
}
