package edu.roomplanner.validation.validator;

public interface UserValidator {

    boolean isRoomIdValid(Long id);

    boolean isRoomEmailValid(String email);

}