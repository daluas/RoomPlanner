package edu.roomplanner.validation.validator;

public interface RightsValidator {

    boolean checkRoomReadRights(String email, Long id);

}
