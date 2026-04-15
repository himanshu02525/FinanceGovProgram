package com.financegov.exceptions;



@SuppressWarnings("serial")
public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String status) {
        super("Invalid status value: " + status + ". Allowed values are Planned, Active, or Closed.");
    }
}
