package com.financegov.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuditStatusConflictException extends RuntimeException {
	public AuditStatusConflictException(String message) {
		super(message);
	}
}