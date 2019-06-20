package edu.roomplanner.rest;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.service.UserService;
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

    private static Logger LOGGER = LogManager.getLogger(UserRestController.class);

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms")
    ResponseEntity<List<RoomDto>> getAllRooms() {
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
        if (roomDto == null) {
            return new ResponseEntity<>(new RoomDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roomDto, HttpStatus.FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    @PreAuthorize("hasAuthority('person') or hasAuthority('room')")
    public ResponseEntity<UserDto> getUserEmailType(@RequestParam(name = "email") String email) {
        Optional<UserDto> userEmailTypeDtoOptional = userService.getUserDto(email);
        return userEmailTypeDtoOptional.
                map(userEmailTypeDto -> new ResponseEntity<>(userEmailTypeDto, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
