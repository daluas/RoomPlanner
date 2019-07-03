package edu.roomplanner.exception;

public class FloorNotFoundException extends RuntimeException {

    public FloorNotFoundException() {
    }

    public FloorNotFoundException(String message) {
        super(message);
    }

}
