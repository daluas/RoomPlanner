package edu.roomplanner.validation.validator;

public interface UserRightsValidator {

    boolean checkIfUserIsRoom();

    boolean checkIfLoggedRoomIsRequestedRoom(Long id);
}