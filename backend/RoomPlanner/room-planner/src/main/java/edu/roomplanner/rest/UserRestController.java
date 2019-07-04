package edu.roomplanner.rest;

import edu.roomplanner.dto.PersonDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.TokenParserService;
import edu.roomplanner.service.UserService;
import edu.roomplanner.validation.validator.UserRightsValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@RestController
@Api(value = "RoomPlanner API", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    private final static Logger LOGGER = LogManager.getLogger(UserRestController.class);

    private final UserService userService;
    private final UserRightsValidator userRightsValidator;
    private final TokenParserService tokenParserService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomDtoMapper roomDtoMapper;

    @Autowired
    public UserRestController(UserService userService, UserRightsValidator userRightsValidator, TokenParserService tokenParserService) {
        this.userService = userService;
        this.userRightsValidator = userRightsValidator;
        this.tokenParserService = tokenParserService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/rooms")
    @ApiOperation("Gets a list with all the rooms in our database")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "FOUND", response = RoomDto.class),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<List<RoomDto>> getAllRooms() {

        LOGGER.info("Method was called.");
        if (userRightsValidator.isUserLoggedAsRoom()) {
            RoomDto roomDto = userService.getRoomByEmail(tokenParserService.getEmailFromToken());
            return new ResponseEntity<>(Collections.singletonList(roomDto), HttpStatus.OK);
        }

        List<RoomDto> allRooms = userService.getAllRooms();
        LOGGER.info("The following object was returned:" + allRooms);
        return new ResponseEntity<>(allRooms, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/rooms/{id}")
    @ApiOperation("Gets a room with an specific id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "FOUND", response = RoomDto.class),
            @ApiResponse(code = 404, message = "This room was not found."),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        LOGGER.info("Method was called.");
        RoomDto roomDto = userService.getRoomById(id);
        LOGGER.info("The following object was returned: " + roomDto);

        if (userRightsValidator.isUserLoggedAsRoom() && !userRightsValidator.isLoggedRoomARequestedRoom(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(roomDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/users")
    @ApiOperation("Gets a user with an specific email")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "This user was not found.", response = PersonDto.class),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    @PreAuthorize("hasAuthority('person') or hasAuthority('room')")
    public ResponseEntity<UserDto> getUserEmailType(@RequestParam(name = "email") String email) {

        LOGGER.info("Method was called.");
        UserDto userEmailTypeDto = userService.getUserDto(email);
        LOGGER.info("The following object was returned: " + userEmailTypeDto);

        if (userRightsValidator.isUserLoggedAsRoom()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(userEmailTypeDto, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/api/rooms/filters")
    @ApiOperation("Returns a list of rooms filtered by reservation start date, reservation end date, room capacity and room floor")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "FOUND", response = RoomDto.class),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PreAuthorize("hasAuthority('person')")
    public ResponseEntity<List<RoomDto>> getRoomsByFilters(@RequestParam @DateTimeFormat(pattern = "EEE',' dd MMM yyyy HH:mm:ss 'GMT'") Calendar startDate,
                                                           @RequestParam @DateTimeFormat(pattern = "EEE',' dd MMM yyyy HH:mm:ss 'GMT'") Calendar endDate,
                                                           @RequestParam(required = false) Integer minPersons,
                                                           @RequestParam(required = false) Integer floor) {

        LOGGER.info("Method was called.");
        List<RoomDto> filteredRooms = userService.getRoomsByFilters(startDate, endDate, minPersons, floor);
        LOGGER.info("The following object was returned: " + filteredRooms);

        return new ResponseEntity<>(filteredRooms, HttpStatus.OK);
    }

}
