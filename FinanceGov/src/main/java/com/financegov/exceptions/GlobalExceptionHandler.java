package com.financegov.exceptions;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Common response body builder , i can use it for showing the op format (body)
	private Map<String, Object> buildBody(HttpStatus status, String message, String errorCode, String path) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", OffsetDateTime.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("errorCode", errorCode);
		body.put("path", path);
		return body;
	}

	// ENTITY / JPA VALIDATION for better messages
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {

		String message = ex.getConstraintViolations().iterator().next().getMessage();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(buildBody(HttpStatus.BAD_REQUEST, message, "VALIDATION_ERROR", request.getRequestURI()));
	}

	// DTO VALIDATION , inside the controller we can see that
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		var details = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> Map.of("field", err.getField(), "message", err.getDefaultMessage())).toList();

		Map<String, Object> body = buildBody(status, "Validation failed. Please review the request.",
				"VALIDATION_ERROR", request.getRequestURI());

		body.put("details", details);

		return ResponseEntity.status(status).body(body);
	}

	// DOMAIN EXCEPTIONS , the main business error that can occur
	@ExceptionHandler({ InvalidAllocationStatusException.class, InvalidResourceStatusException.class })
	public ResponseEntity<?> handleDomainExceptions(RuntimeException ex, HttpServletRequest request) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(buildBody(HttpStatus.BAD_REQUEST, ex.getMessage(), "DOMAIN_ERROR", request.getRequestURI()));
	}

	@ExceptionHandler(AllocationNotFoundException.class)
	public ResponseEntity<?> handleNotFound(AllocationNotFoundException ex, HttpServletRequest request) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(buildBody(HttpStatus.NOT_FOUND, ex.getMessage(), "RESOURCE_NOT_FOUND", request.getRequestURI()));
	}

	// FALLBACK , never give ful stack trace
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneric(Exception ex, HttpServletRequest request) {

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildBody(HttpStatus.INTERNAL_SERVER_ERROR,
				"Unexpected error occurred. Please try again later.", "UNEXPECTED_ERROR", request.getRequestURI()));
	}
}
