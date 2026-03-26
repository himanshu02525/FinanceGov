package com.financegov.exceptions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import tools.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {

	/* ============= Audit Exception Handler ============= */

	@ExceptionHandler(exception = AuditRecordNotFoundException.class)
	public ResponseEntity<ExceptionResponse> auditRecordNotFound(AuditRecordNotFoundException ex) {
		ExceptionResponse exception = new ExceptionResponse(ex.getMessage(), LocalDate.now(),
				HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(exception = AuditStatusConflictException.class)
	public ResponseEntity<ExceptionResponse> auditStatusConflictException(AuditStatusConflictException ex) {
		ExceptionResponse exception = new ExceptionResponse(ex.getMessage(), LocalDate.now(),
				HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
	}

	/* ============= Compliance Exception Handler ============= */

	@ExceptionHandler(exception = ComplianceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> complianceNotFound(ComplianceNotFoundException ex) {
		ExceptionResponse exception = new ExceptionResponse(ex.getMessage(), LocalDate.now(),
				HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(exception = ComplianceStatusConflictException.class)
	public ResponseEntity<ExceptionResponse> complianceStatusConflictException(ComplianceStatusConflictException ex) {
		ExceptionResponse exception = new ExceptionResponse(ex.getMessage(), LocalDate.now(),
				HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
	}

	/* ============= Other Exception Handler ============= */

	@ExceptionHandler(exception = InvalidFormatException.class)
	public ResponseEntity<ExceptionResponse> invalidFormatException(InvalidFormatException ex) {
		ExceptionResponse exception = new ExceptionResponse(ex.getMessage(), LocalDate.now(),
				HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + " : " + err.getDefaultMessage()).toList();

		Map<String, Object> body = new HashMap<>();
		body.put("errors", errors);
		body.put("statusCode", HttpStatus.BAD_REQUEST.value());
		body.put("dateOfException", LocalDate.now());

		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponse> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
		String userFriendlyMessage = "Malformed JSON request or invalid data format";

		ExceptionResponse exception = new ExceptionResponse(userFriendlyMessage, LocalDate.now(),
				HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<ExceptionResponse> handleGlobalException(Exception ex) {
		ExceptionResponse exception = new ExceptionResponse("An internal server error occurred", LocalDate.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}