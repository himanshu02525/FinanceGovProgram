package com.financegov.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when a user attempts to file taxes 
 * for a year outside the allowed window (Current or Previous).
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTaxYearException extends RuntimeException {
    public InvalidTaxYearException(String message) {
        super(message);
    }
}