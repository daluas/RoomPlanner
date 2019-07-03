package edu.roomplanner.exception;

public class UnauthorizedReservationException extends RuntimeException {
    public UnauthorizedReservationException() {
    }

    public UnauthorizedReservationException(String message) {
        super(message);
    }
}
