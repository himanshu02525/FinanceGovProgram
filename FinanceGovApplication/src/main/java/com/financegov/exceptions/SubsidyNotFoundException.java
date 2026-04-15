package com.financegov.exceptions;

@SuppressWarnings("serial")
public class SubsidyNotFoundException extends RuntimeException {
    public SubsidyNotFoundException(Long id) {
        super("Subsidy with ID " + id + " not found");
    }
}

