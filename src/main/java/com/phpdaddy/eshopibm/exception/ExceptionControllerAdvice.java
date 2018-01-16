package com.phpdaddy.eshopibm.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        BindingResult errors = ex.getBindingResult();
        for (FieldError e : errors.getFieldErrors()) {
            errorMap.put(e.getField(), e.getDefaultMessage());
        }
        ex.printStackTrace();
        ValidationError validationError = new ValidationError(errorMap);
        return new ResponseEntity<>(validationError, getStatusType(ex));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
        for (ConstraintViolation e : errors) {
            errorMap.put(e.getPropertyPath().toString(), e.getMessage());
        }
        ex.printStackTrace();
        ValidationError validationError = new ValidationError(errorMap);
        return new ResponseEntity<>(validationError, getStatusType(ex));
    }

    @ExceptionHandler(value = {TransactionSystemException.class})
    public ResponseEntity<?> handleTxException(TransactionSystemException ex) {
        Error error;
        Throwable t = ex.getRootCause();
        if (t instanceof ConstraintViolationException) {
            return constraintViolationExceptionHandler((ConstraintViolationException) t);
        } else {
            ex.printStackTrace();
            error = new Error(ex.getLocalizedMessage());
        }
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception ex) {
        Error error = new Error(ex.getLocalizedMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(error, getStatusType(ex));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Error error = new Error(ex.toString());
        ex.printStackTrace();
        return new ResponseEntity<>(error, getStatusType(ex));
    }

    private static HttpStatus getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getStatus();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}