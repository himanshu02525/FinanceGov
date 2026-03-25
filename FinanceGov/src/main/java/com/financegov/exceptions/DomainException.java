package com.financegov.exceptions;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")

public abstract class DomainException extends RuntimeException {
	private final String errorCode;
	private final HttpStatus status;

	protected DomainException(String message, String errorCode, HttpStatus status) {
		super(message);
		this.errorCode = errorCode;
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
