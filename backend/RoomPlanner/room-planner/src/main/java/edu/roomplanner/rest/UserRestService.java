package edu.roomplanner.rest;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.service.UserService;
import edu.roomplanner.service.ValidationService;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EqualsAndHashCode
@RestController
public class UserRestService {

    private static Logger restLogger = LogManager.getLogger(UserRestService.class);

    private final UserService userService;
    private final ValidationService validationService;

    @Autowired
    public UserRestService(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms")
    ResponseEntity<List<RoomDto>> getAllRooms() {
        restLogger.info("Method was called.");
        List<RoomDto> allRoomsList = userService.getAllRooms();
        restLogger.info("The following object was returned:" + allRoomsList);
        return new ResponseEntity<>(allRoomsList, HttpStatus.FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{id}")
    ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        RoomDto roomDto = new RoomDto();
        if (validationService.checkValidRoomId(id)) {
            roomDto = userService.getRoomById(id);
            restLogger.info("Method was called.");
            restLogger.info("The following object was returned: " + roomDto);
            return new ResponseEntity<>(roomDto, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(roomDto, HttpStatus.NOT_FOUND);
    }
}
