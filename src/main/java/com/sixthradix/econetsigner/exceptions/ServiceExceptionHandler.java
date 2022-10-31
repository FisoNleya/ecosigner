package com.sixthradix.econetsigner.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> dataNotFoundExceptionHandler(DataNotFoundException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Requested data not found")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error(customError.toString(), ex);
        return ResponseEntity.badRequest().body(customError);
    }


    @ExceptionHandler(ApplicationRequestException.class)
    public ResponseEntity<Object> applicationRequestExceptionHandle(ApplicationRequestException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Check input data")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error(customError.toString(), ex);
        return ResponseEntity.badRequest().body(customError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Part of the input data is not allowed")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error(customError.toString(), ex);
        return ResponseEntity.badRequest().body(customError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedExceptionHandler(AccessDeniedException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("User not allowed to manipulate data")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.FORBIDDEN)
                .build();
        log.error(customError.toString(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customError);
    }



    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Object> jsonProcessingExceptionHandler(JsonProcessingException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.EXPECTATION_FAILED)
                .build();
        log.error(customError.toString(), ex);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(customError);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> generalSqlExceptionHandler(SQLException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Failed to perform operations at database layer")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.EXPECTATION_FAILED)
                .build();
        log.error(customError.toString(), ex);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(customError);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Object> parseExceptionHandler(ParseException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Illegal data parsed")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error(customError.toString(), ex);
        return ResponseEntity.badRequest().body(customError);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> nullPointerExceptionHandler(NullPointerException ex) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Null object encountered and requested for manipulation")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        log.error(customError.toString(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customError);
    }


    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage customError = ErrorMessage.builder()
                .eventTime(ZonedDateTime.now())
                .error("Wrong request type launched")
                .errorDescription(ex.getLocalizedMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error(customError.toString(), ex);
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

}
