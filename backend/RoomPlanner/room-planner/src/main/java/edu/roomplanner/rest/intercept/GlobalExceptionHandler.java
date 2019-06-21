package edu.roomplanner.rest.intercept;


import edu.roomplanner.exception.UnknownUserTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UnknownUserTypeException.class})
    public ResponseEntity<String> handleException(UnknownUserTypeException exception) {

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
