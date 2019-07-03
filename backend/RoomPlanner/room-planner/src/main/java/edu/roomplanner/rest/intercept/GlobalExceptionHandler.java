package edu.roomplanner.rest.intercept;

import edu.roomplanner.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private class JsonResponse {
        String error;

        public JsonResponse() {
        }

        public JsonResponse(String message) {
            super();
            this.error = message;
        }

        public String getMessage() {
            return error;
        }

    }

    @ExceptionHandler({UnknownUserTypeException.class})
    public ResponseEntity<JsonResponse> handleUnknownUserTypeException(UnknownUserTypeException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<JsonResponse> handleUserNotFoundException(UserNotFoundException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidReservationDtoException.class})
    public ResponseEntity<JsonResponse> handleInvalidReservationDtoException(InvalidReservationDtoException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidReservationException.class})
    public ResponseEntity<JsonResponse> handleInvalidReservationException(InvalidReservationException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ReservationNotFoundException.class})
    public ResponseEntity<JsonResponse> handleReservationNotFoundException(ReservationNotFoundException exception) {
        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({FloorNotFoundException.class})
    public ResponseEntity<JsonResponse> handleFloorNotFoundException(FloorNotFoundException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnauthorizedReservationException.class})
    public ResponseEntity<JsonResponse> handleUnauthorizedReservationException(UnauthorizedReservationException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({RoomNotFoundException.class})
    public ResponseEntity<JsonResponse> handleRoomNotFoundException(RoomNotFoundException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotPersonException.class})
    public ResponseEntity<JsonResponse> handleNotPersonException(NotPersonException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidDateException.class})
    public ResponseEntity<JsonResponse> handleInvalidDateExceptionn(InvalidDateException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PersonIdOfReservationNotEqualsUserIdException.class})
    public ResponseEntity<JsonResponse> handlePersonIdOfReservationNotEqualsUserIdException(PersonIdOfReservationNotEqualsUserIdException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserAuthorityException.class})
    public ResponseEntity<JsonResponse> handleUserAuthorityException(UserAuthorityException exception) {

        return new ResponseEntity<>(new JsonResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
