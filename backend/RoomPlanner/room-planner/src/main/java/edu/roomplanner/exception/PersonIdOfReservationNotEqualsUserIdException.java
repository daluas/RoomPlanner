package edu.roomplanner.exception;

public class PersonIdOfReservationNotEqualsUserIdException extends RuntimeException {
    public PersonIdOfReservationNotEqualsUserIdException() {
    }

    public PersonIdOfReservationNotEqualsUserIdException(String message) {
        super(message);
    }
}

