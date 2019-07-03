package edu.roomplanner.validation.validator;

public interface UserRightsValidator {

    boolean isUserLoggedAsRoom();

    boolean isLoggedRoomARequestedRoom(Long id);

}