package edu.roomplanner.service;

public interface ValidationService {

    boolean checkValidRoomId(Long id);

    boolean checkExistingRoomId(Long id);
}
