package com.web.cloudtube.core.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;


@RestControllerAdvice
public class ApplicationServerErrorHandlerAdvice {
    static final Logger logger = LoggerFactory.getLogger(ApplicationServerErrorHandlerAdvice.class);

    @ExceptionHandler(value = {ResponseStatusException.class, ServletException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<HttpErrorResponseWrapper> httpStatusError(Exception e) {
        logger.debug("Handle server exception: " + e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
