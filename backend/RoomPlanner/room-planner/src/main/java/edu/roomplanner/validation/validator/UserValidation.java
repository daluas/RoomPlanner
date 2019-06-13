package edu.roomplanner.validation.validator;

public interface UserValidation {

    boolean checkValidRoomId(Long id);

    boolean checkExistingEntityId(Long id);
}
