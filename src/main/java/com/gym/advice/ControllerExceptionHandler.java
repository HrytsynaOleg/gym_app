package com.gym.advice;

import com.gym.exception.IncorrectCredentialException;
import com.gym.dto.ResponseErrorBodyDTO;
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

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorBodyDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.error("Argument validation error: {}. Transaction Id {}", getErrorsLoggerDescription(errors),
                MDC.get("transactionId"));
        return new ResponseEntity<>(getErrorsBody(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseErrorBodyDTO> handleHttpRequestJsonErrors(HttpMessageNotReadableException ex) {
        List<String> errors = List.of(ex.getMessage());
        log.error("Http request json error: {}. Transaction Id {}", getErrorsLoggerDescription(errors),
                MDC.get("transactionId"));
        return new ResponseEntity<>(getErrorsBody(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectCredentialException.class)
    public ResponseEntity<ResponseErrorBodyDTO> handleHttpRequestUnauthorizedErrors(IncorrectCredentialException ex) {
        List<String> errors = List.of(ex.getMessage());
        log.error("User unauthorized error: {}. Transaction Id {}", getErrorsLoggerDescription(errors),
                MDC.get("transactionId"));
        return new ResponseEntity<>(getErrorsBody(errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    private String getErrorsLoggerDescription(List<String> errors) {
        StringJoiner joiner = new StringJoiner(", ");
        errors.forEach(joiner::add);
        return joiner.toString();
    }

    private ResponseErrorBodyDTO getErrorsBody(List<String> errors) {
        return ResponseErrorBodyDTO.builder()
                .errors(errors)
                .build();
    }
}
