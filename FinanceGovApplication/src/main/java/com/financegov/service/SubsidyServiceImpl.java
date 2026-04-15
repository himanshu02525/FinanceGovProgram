package com.financegov.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financegov.dto.SubsidyRequest;
import com.financegov.dto.SubsidyResponse;
import com.financegov.enums.SubsidyStatus;
import com.financegov.exceptions.SubsidyNotFoundException;
import com.financegov.model.CitizenBusiness;
import com.financegov.model.FinancialProgram;
import com.financegov.model.Subsidy;
import com.financegov.repository.CitizenBusinessRepository;
import com.financegov.repository.FinancialProgramRepository;
import com.financegov.repository.SubsidyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubsidyServiceImpl implements SubsidyService {

    
    private final SubsidyRepository subsidyRepository;

    
    private final FinancialProgramRepository programRepository;
    
    private final CitizenBusinessRepository citizenBusinessRepository;

//    @Override
//    @Transactional
//    public SubsidyResponse saveSubsidy(SubsidyRequest request) {
//        FinancialProgram program = programRepository.findById(request.getProgramId())
//                .orElseThrow(() -> new IllegalArgumentException("Program not found"));
//
//        if (request.getAmount() > program.getBudget()) {
//            throw new IllegalStateException("Requested amount (" + request.getAmount() +
//                    ") exceeds program budget (" + program.getBudget() + "). Subsidy cannot be granted.");
//        }
//
//        Subsidy subsidy = new Subsidy();
//        subsidy.setEntityId(request.getEntityId());
//        subsidy.setAmount(request.getAmount());
//        subsidy.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
//        subsidy.setStatus(SubsidyStatus.valueOf(request.getStatus().toUpperCase()));
//        subsidy.setProgram(program);
//
//        Subsidy savedSubsidy = subsidyRepository.save(subsidy);
//        return toResponse(savedSubsidy);
//    }
    
    
    @Override
    @Transactional
    public SubsidyResponse saveSubsidy(SubsidyRequest request) {
        FinancialProgram program = programRepository.findById(request.getProgramId())
                .orElseThrow(() -> new IllegalArgumentException("Program not found"));

        if (request.getAmount() > program.getBudget()) {
            throw new IllegalStateException("Requested amount (" + request.getAmount() +
                    ") exceeds program budget (" + program.getBudget() + "). Subsidy cannot be granted.");
        }

        
        CitizenBusiness entity = citizenBusinessRepository.findById(request.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Entity with ID " + request.getEntityId() + " not found"));

        Subsidy subsidy = new Subsidy();
        subsidy.setCitizenBusiness(entity);   
        subsidy.setAmount(request.getAmount());
        subsidy.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        subsidy.setStatus(SubsidyStatus.valueOf(request.getStatus().toUpperCase()));
        subsidy.setProgram(program);

        Subsidy savedSubsidy = subsidyRepository.save(subsidy);
        return toResponse(savedSubsidy);
    }


    @Override
    public List<SubsidyResponse> getAllSubsidies() {
        return subsidyRepository.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
    }

    @Override
    public List<SubsidyResponse> getSubsidiesByProgram(Long programId) {
        return subsidyRepository.findByProgramProgramId(programId)
                                .stream()
                                .map(this::toResponse)
                                .toList();
    }
    
    @Override
    public List<SubsidyResponse> getSubsidiesByEntity(Long entityId) {
        return subsidyRepository.findByCitizenBusinessEntityId(entityId)   
                                .stream()
                                .map(this::toResponse)
                                .toList();
    }



//    @Override
//    public SubsidyResponse updateSubsidyStatus(Long subsidyId, String status) {
//        Subsidy subsidy = subsidyRepository.findById(subsidyId)
//                .orElseThrow(() -> new SubsidyNotFoundException(subsidyId));
//
//        try {
//            SubsidyStatus newStatus = SubsidyStatus.valueOf(status.toUpperCase());
//            subsidy.setStatus(newStatus);
//        } catch (IllegalArgumentException ex) {
//            throw new IllegalStateException("Invalid status: " + status + ". Allowed values: GRANTED, CANCELLED");
//        }
//
//        Subsidy updated = subsidyRepository.save(subsidy);
//        return toResponse(updated);
//    }

    @Override
    public SubsidyResponse getSubsidyById(Long subsidyId) {
        Subsidy subsidy = subsidyRepository.findById(subsidyId)
                .orElseThrow(() -> new SubsidyNotFoundException(subsidyId));
        return toResponse(subsidy);
    }

    private SubsidyResponse toResponse(Subsidy subsidy) {
        return new SubsidyResponse(
            subsidy.getSubsidyId(),
            subsidy.getCitizenBusiness().getEntityId(),  
            subsidy.getAmount(),
            subsidy.getDate(),
            subsidy.getStatus().name(),
            subsidy.getProgram().getProgramId()
        );
    }

}
