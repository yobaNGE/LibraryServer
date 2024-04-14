package com.example.libraryserver.exceptions;

import com.example.libraryserver.responses.general.InfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<InfoResponse> catchResourceNotFoundException(ResourceNotFoundException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new InfoResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<InfoResponse> catchStatusConflictException(StatusConflictException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new InfoResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<InfoResponse> catchDatabaseConnectionException(DatabaseConnectionException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new InfoResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

//    @ExceptionHandler
//    public ResponseEntity<String> catchResourceNotAvailableException(ResourceNotAvailableException e){
//        log.error(e.getMessage(), e);
//        return ResponseEntity.status(HttpStatus.Not)
//    }
}