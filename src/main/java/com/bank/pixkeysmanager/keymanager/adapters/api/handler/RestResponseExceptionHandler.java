package com.bank.pixkeysmanager.keymanager.adapters.api.handler;

import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerKeyNotFountException;
import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerQueryWithoutResultsException;
import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        log.error(String.valueOf(exception));
        final List<String> formattedErrors = exception.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        return new ResponseEntity(Map.of("errors", formattedErrors), headers, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> processValidationArguments(final ConstraintViolationException exception) {

        log.error(String.valueOf(exception));
        final List<String> formattedErrors = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("errors", formattedErrors));
    }

    @ExceptionHandler({KeyManagerValidationException.class, KeyManagerKeyNotFountException.class, KeyManagerQueryWithoutResultsException.class})
    public ResponseEntity<Map> processValidationArguments(final KeyManagerValidationException exception) {
        log.error(String.valueOf(exception));

        HttpStatus statusResponse = null;

        if (exception instanceof KeyManagerValidationException) {
            statusResponse = HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (exception instanceof KeyManagerKeyNotFountException) {
            statusResponse = HttpStatus.NOT_FOUND;
        } else if (exception instanceof KeyManagerQueryWithoutResultsException) {
            statusResponse = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(statusResponse).body(Map.of("errors", exception.getErrors()));
    }

}

