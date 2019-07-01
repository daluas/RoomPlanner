package edu.roomplanner.exception;

public class InvalidReservationException extends RuntimeException {

    public InvalidReservationException() {
    }

    public InvalidReservationException(String message) {
        super(message);
    }

}
