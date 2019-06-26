package edu.roomplanner.rest;

import edu.roomplanner.dto.PersonDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.service.TokenParserService;
import edu.roomplanner.service.UserService;
import edu.roomplanner.validation.validator.UserRightsValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserRestController {

    private final static Logger LOGGER = LogManager.getLogger(UserRestController.class);

    private final UserService userService;
    private final UserRightsValidator userRightsValidator;
    private final TokenParserService tokenParserService;

    @Autowired
    public UserRestController(UserService userService, UserRightsValidator userRightsValidator, TokenParserService tokenParserService) {
        this.userService = userService;
        this.userRightsValidator = userRightsValidator;
        this.tokenParserService = tokenParserService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms")
    ResponseEntity<List<RoomDto>> getAllRooms() {

        if (userRightsValidator.checkIfUserIsRoom()) {
            RoomDto roomDto = userService.getRoomByEmail(tokenParserService.getEmailFromToken());
            return new ResponseEntity(roomDto, HttpStatus.FOUND);
        }
        LOGGER.info("Method was called.");
        List<RoomDto> allRooms = userService.getAllRooms();
        LOGGER.info("The following object was returned:" + allRooms);
        return new ResponseEntity<>(allRooms, HttpStatus.FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{id}")
    ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        LOGGER.info("Method was called.");
        RoomDto roomDto = userService.getRoomById(id);
        LOGGER.info("The following object was returned: " + roomDto);
        if(userRightsValidator.checkIfUserIsRoom() && !userRightsValidator.checkIfLoggedRoomIsRequestedRoom(id)){
            return new ResponseEntity<>(new RoomDto(), HttpStatus.UNAUTHORIZED);
        }
        if (roomDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roomDto, HttpStatus.FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    @PreAuthorize("hasAuthority('person') or hasAuthority('room')")
    public ResponseEntity<UserDto> getUserEmailType(@RequestParam(name = "email") String email) {
        Optional<UserDto> userEmailTypeDtoOptional = userService.getUserDto(email);
        if(userRightsValidator.checkIfUserIsRoom()){
            return new ResponseEntity<>(new PersonDto(), HttpStatus.UNAUTHORIZED);
        }
        return userEmailTypeDtoOptional.
                map(userEmailTypeDto -> new ResponseEntity<>(userEmailTypeDto, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
