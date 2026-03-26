package com.financegov.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND) 
public class ComplianceStatusConflictException extends RuntimeException{
    public ComplianceStatusConflictException(String message) {
        super(message);
    }
}