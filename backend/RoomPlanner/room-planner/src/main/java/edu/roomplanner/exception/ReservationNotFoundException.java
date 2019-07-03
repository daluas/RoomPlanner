package edu.roomplanner.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException() {
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }

}
