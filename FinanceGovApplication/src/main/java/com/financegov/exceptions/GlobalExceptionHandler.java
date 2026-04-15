package com.financegov.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    /* ==========================================================
       COMMON JSON RESPONSE WRITER (FOR SECURITY FILTER ERRORS)
       ========================================================== */
    private void writeSecurityError(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        response.setStatus(status.value());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), body);
    }

    /* ==========================================================
       401 HANDLER (UNAUTHORIZED) - AuthenticationEntryPoint
       ========================================================== */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.warn("Authentication failed: {}", authException.getMessage());

        writeSecurityError(
                response,
                HttpStatus.UNAUTHORIZED,
                "Authentication failed. Please login with valid credentials."
        );
    }

    /* ==========================================================
       403 HANDLER (FORBIDDEN) - AccessDeniedHandler
       ========================================================== */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        log.warn("Access denied: {}", ex.getMessage());

        writeSecurityError(
                response,
                HttpStatus.FORBIDDEN,
                "You do not have permission to perform this action."
        );
    }


    /* ==========================================================
       AUTH MODULE EXCEPTIONS
       ========================================================== */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse("Invalid email or password.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    /* ==========================================================
       VALIDATION EXCEPTIONS
       ========================================================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " : " + err.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Validation failed");
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }


    /* ==========================================================
       JSON PARSING / JACKSON ERRORS
       ========================================================== */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> invalidFormat(InvalidFormatException ex) {
        return buildResponse("Invalid input format: " + ex.getOriginalMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> jsonNotReadable(HttpMessageNotReadableException ex) {
        return buildResponse("Malformed JSON request or invalid data type",
                HttpStatus.BAD_REQUEST);
    }


    /* ==========================================================
       AUDIT MODULE
       ========================================================== */
    @ExceptionHandler(AuditRecordNotFoundException.class)
    public ResponseEntity<ExceptionResponse> auditRecordNotFound(AuditRecordNotFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuditStatusConflictException.class)
    public ResponseEntity<ExceptionResponse> auditStatusConflict(AuditStatusConflictException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }


    /* ==========================================================
       COMPLIANCE MODULE
       ========================================================== */
    @ExceptionHandler(ComplianceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> complianceNotFound(ComplianceNotFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ComplianceStatusConflictException.class)
    public ResponseEntity<ExceptionResponse> complianceStatusConflict(ComplianceStatusConflictException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ComplianceViolationException.class)
    public ResponseEntity<ExceptionResponse> complianceViolation(ComplianceViolationException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /* ==========================================================
       PROGRAM MODULE
       ========================================================== */
    @ExceptionHandler(ProgramNotFoundException.class)
    public ResponseEntity<ExceptionResponse> programNotFound(ProgramNotFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProgramApplicationsNotFoundException.class)
    public ResponseEntity<ExceptionResponse> programApplicationsNotFound(ProgramApplicationsNotFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ExceptionResponse> invalidProgramStatus(InvalidStatusException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /* ==========================================================
       SUBSIDY MODULE
       ========================================================== */
    @ExceptionHandler(SubsidyNotFoundException.class)
    public ResponseEntity<ExceptionResponse> subsidyNotFound(SubsidyNotFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    /* ==========================================================
       TAX MODULE / RESOURCE MODULE
       ========================================================== */
    @ExceptionHandler(InvalidResourceStatusException.class)
    public ResponseEntity<ExceptionResponse> invalidResourceStatus(InvalidResourceStatusException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /* ==========================================================
       ALLOCATION MODULE
       ========================================================== */
    @ExceptionHandler(AllocationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> allocationNotFound(AllocationNotFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAllocationStatusException.class)
    public ResponseEntity<ExceptionResponse> invalidAllocationStatus(InvalidAllocationStatusException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /* ==========================================================
       ENTITY MODULE
       ========================================================== */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> entityNotFound(EntityNotFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    /* ==========================================================
       BUSINESS EXCEPTIONS (Runtime)
       ========================================================== */
    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<Object> handleBusinessExceptions(RuntimeException ex) {
        log.warn("Business error: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /* ==========================================================
       GLOBAL FALLBACK EXCEPTION
       ========================================================== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUnhandled(Exception ex) {

        log.error("Unexpected system error", ex);

        return buildResponse(
                "Internal server error. Please contact support.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    /* ==========================================================
       RESPONSE BUILDERS
       ========================================================== */
    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<ExceptionResponse> buildExceptionResponse(String message, HttpStatus status) {
        ExceptionResponse exception = new ExceptionResponse(message, LocalDate.now(), status.value());
        return new ResponseEntity<>(exception, status);
    }

}