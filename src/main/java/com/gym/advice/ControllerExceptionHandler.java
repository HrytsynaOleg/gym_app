package com.gym.advice;

import com.gym.exception.IncorrectCredentialException;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.error("Argument validation error: {}. Transaction Id {}", getErrorsLoggerDescription(errors),
                MDC.get("transactionId"));
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, List<String>>> handleHttpRequestJsonErrors(HttpMessageNotReadableException ex) {
        List<String> errors = List.of(ex.getMessage());
        log.error("Http request json error: {}. Transaction Id {}", getErrorsLoggerDescription(errors),
                MDC.get("transactionId"));
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectCredentialException.class)
    public ResponseEntity<Map<String, List<String>>> handleHttpRequestUnauthorizedErrors(IncorrectCredentialException ex) {
        List<String> errors = List.of(ex.getMessage());
        log.error("User unauthorized error: {}. Transaction Id {}", getErrorsLoggerDescription(errors),
                MDC.get("transactionId"));
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    private String getErrorsLoggerDescription(List<String> errors) {
        StringJoiner joiner = new StringJoiner(", ");
        errors.forEach(joiner::add);
        return joiner.toString();
    }
}
