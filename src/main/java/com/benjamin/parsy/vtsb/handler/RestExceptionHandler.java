package com.benjamin.parsy.vtsb.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseRecord> handleException(Exception ex) {

        log.error("An unexpected error has occurred", ex);

        return ResponseEntity.internalServerError()
                .body(new ErrorResponseRecord(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

}
