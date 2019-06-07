package edu.roomplanner.rest;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms")
    List<RoomDto> getAllRooms() {
        return userService.getAllRooms();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{id}")
    RoomDto getRoomById(@PathVariable Integer id) {
        return userService.getRoomById(id);
    }
}
