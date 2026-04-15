package com.financegov.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financegov.dto.NotificationRequestDto;
import com.financegov.dto.SubsidyApplicationRequest;
import com.financegov.dto.SubsidyApplicationResponse;
import com.financegov.enums.ApplicationStatus;
import com.financegov.enums.NotificationCategory;
import com.financegov.enums.ProgramStatus;
import com.financegov.enums.RoleType;
import com.financegov.exceptions.ApplicationNotFoundException;
import com.financegov.model.CitizenBusiness;
import com.financegov.model.FinancialProgram;
import com.financegov.model.SubsidyApplication;
import com.financegov.model.User;
import com.financegov.repository.CitizenBusinessRepository;
import com.financegov.repository.FinancialProgramRepository;
import com.financegov.repository.SubsidyApplicationRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@Service
@RequiredArgsConstructor
public class SubsidyApplicationServiceImpl implements SubsidyApplicationService {

	private final FinancialProgramRepository programRepository;

	private final NotificationService notificationService;

	private final SubsidyApplicationRepository applicationRepository;

	private final CitizenBusinessRepository citizenBusinessRepository;

	private final UserService userService;

	@Override
	@Transactional
	public SubsidyApplicationResponse saveApplication(SubsidyApplicationRequest request) {
//		FinancialProgram program = programRepository.findById(request.getProgramId())
//				.orElseThrow(() -> new IllegalArgumentException("Program not found"));
//
//		if (program.getStatus() == ProgramStatus.CLOSED) {
//			throw new IllegalStateException("Applications cannot be submitted. Program is CLOSED.");
//		}
//
//		SubsidyApplication app = new SubsidyApplication();
//		app.setEntityId(request.getEntityId());
//		app.setSubmittedDate(request.getSubmittedDate());
//		app.setStatus(request.getStatus() != null ? request.getStatus() : ApplicationStatus.PENDING);
//		app.setProgram(program);

		CitizenBusiness entity = citizenBusinessRepository.findById(request.getEntityId()).orElseThrow(
				() -> new IllegalArgumentException("Entity with ID " + request.getEntityId() + " not found"));

		FinancialProgram program = programRepository.findById(request.getProgramId())
				.orElseThrow(() -> new IllegalArgumentException("Program not found"));

		if (program.getStatus() == ProgramStatus.CLOSED) {
			throw new IllegalStateException("Applications cannot be submitted. Program is CLOSED.");
		}

		SubsidyApplication app = new SubsidyApplication();
		app.setCitizenBusiness(entity); 
		app.setSubmittedDate(request.getSubmittedDate());
		app.setStatus(ApplicationStatus.PENDING);
		app.setProgram(program);

		SubsidyApplication saved = applicationRepository.save(app);

		List<User> users = userService.findAllUsers();

		users.stream().filter(
				user -> user.getRole() != null && user.getRole().getRoleName() == RoleType.ROLE_FINANCIAL_OFFICER)
				.forEach(officer -> {

					NotificationRequestDto notification = NotificationRequestDto.builder().userId(officer.getId()) // Receiver
							.entityId(saved.getCitizenBusiness().getEntityId()) 
							.category(NotificationCategory.SUBSIDY)
							.message("A new Citizen/Business has APPLIED for a subsidy and is pending approval")
							.build();

					notificationService.sendNotification(notification, officer.getEmail());
				});

		return toResponse(saved);

	}

//	@Override
//	public SubsidyApplicationResponse approveApplication(Long applicationId) {
//		SubsidyApplication app = applicationRepository.findById(applicationId)
//				.orElseThrow(() -> new ApplicationNotFoundException(applicationId));
//
//		if (app.getStatus() == ApplicationStatus.REJECTED) {
//			throw new IllegalStateException("Rejected applications cannot be approved.");
//		}
//
//		app.setStatus(ApplicationStatus.APPROVED);
//		SubsidyApplication updated = applicationRepository.save(app);
//		return toResponse(updated);
//	}

	@Override
	public SubsidyApplicationResponse approveApplication(Long applicationId) {
		SubsidyApplication app = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new ApplicationNotFoundException(applicationId));

		if (app.getStatus() == ApplicationStatus.REJECTED) {
			throw new IllegalStateException("Rejected applications cannot be approved.");
		}

		app.setStatus(ApplicationStatus.APPROVED);
		SubsidyApplication updated = applicationRepository.save(app);

		return toResponse(updated);
	}

//
//	@Override
//	public SubsidyApplicationResponse rejectApplication(Long applicationId) {
//		SubsidyApplication app = applicationRepository.findById(applicationId)
//				.orElseThrow(() -> new ApplicationNotFoundException(applicationId));
//		
//		// Only pending applications can be rejected
//        if (app.getStatus() != ApplicationStatus.PENDING) {
//            throw new IllegalStateException("Only pending applications can be rejected. Approved applications cannot be rejected.");
//        }
//		
//		app.setStatus(ApplicationStatus.REJECTED);
//		SubsidyApplication updated = applicationRepository.save(app);
//		return toResponse(updated);
//	}

	@Override
	public SubsidyApplicationResponse rejectApplication(Long applicationId) {
		SubsidyApplication app = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new ApplicationNotFoundException(applicationId));

		if (app.getStatus() != ApplicationStatus.PENDING) {
			throw new IllegalStateException(
					"Only pending applications can be rejected. Approved applications cannot be rejected.");
		}

		app.setStatus(ApplicationStatus.REJECTED);
		SubsidyApplication updated = applicationRepository.save(app);

		return toResponse(updated);
	}

	private SubsidyApplicationResponse toResponse(SubsidyApplication app) {
		return new SubsidyApplicationResponse(app.getApplicationId(), app.getCitizenBusiness().getEntityId(), // ✅
																												// mapped
																												// entity
				app.getSubmittedDate(), app.getProgram().getProgramId(), app.getStatus());
	}

	@Override
	public List<SubsidyApplicationResponse> getApplicationsByEntity(Long entityId) {
		return applicationRepository.findByCitizenBusinessEntityId(entityId).stream().map(this::toResponse).toList();
	}

}
