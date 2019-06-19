package edu.roomplanner.rest.intercept;


import edu.roomplanner.exception.UnknownUserTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UnknownUserTypeException.class})
    public ResponseEntity handleException(Exception exception) {
        UnknownUserTypeException unknownUserTypeException = (UnknownUserTypeException) exception;

        return new ResponseEntity(unknownUserTypeException.getMessage(), INTERNAL_SERVER_ERROR);
    }
}
