package edu.roomplanner.rest;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.service.UserService;
import edu.roomplanner.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestService {

    static Logger restLogger = LoggerFactory.getLogger(UserRestService.class);

    private final UserService userService ;
    private final ValidationService validationService;

    @Autowired
    public UserRestService(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms")
    List<RoomDto> getAllRooms() {
        restLogger.info("getAllRooms was called.");
        List<RoomDto> allRoomsList =  userService.getAllRooms();
        restLogger.info("getAllRooms returned: " +  allRoomsList);
        return allRoomsList;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{id}")
    ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        RoomDto roomDto = new RoomDto();
        if(validationService.checkValidRoomId(id)){
            roomDto = userService.getRoomById(id);
            return new ResponseEntity<>(roomDto, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(roomDto, HttpStatus.NOT_FOUND);
    }
}
