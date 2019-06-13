package edu.roomplanner.validation;

public interface UserValidation {

    boolean checkValidRoomId(Long id);

    boolean checkExistingRoomId(Long id);
}
