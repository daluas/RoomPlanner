package edu.roomplanner.exception;

public class InvalidReservationDtoException extends RuntimeException {

    public InvalidReservationDtoException() {
    }

    public InvalidReservationDtoException(String message) {
        super(message);
    }

}
