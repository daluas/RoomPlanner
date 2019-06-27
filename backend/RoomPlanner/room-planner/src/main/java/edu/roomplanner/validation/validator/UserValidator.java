package edu.roomplanner.validation.validator;

public interface UserValidator {

    boolean checkValidRoomId(Long id);

    boolean checkValidRoomEmail(String email);
}