package edu.roomplanner.rest;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(value = "Reservation API", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private static final Logger LOGGER = LogManager.getLogger(ReservationController.class);

    private final ReservationService bookRoomService;

    @Autowired
    public ReservationController(ReservationService bookRoomService) {
        this.bookRoomService = bookRoomService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/reservations/{room_id}")
    @ApiOperation("Books a reservation for a room with a specific id.")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Reservation was booked successfully."),
            @ApiResponse(code = 404, message = "This room was not found."),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    @PreAuthorize("hasAuthority('person')")
    ResponseEntity<ReservationDto> getReservationCreated(@PathVariable(name = "room_id") Long roomId,
                                                         @RequestBody ReservationDto reservationDto) {

        LOGGER.info("Method was called.");
        Optional<ReservationDto> reservationDtoOptional = bookRoomService.createReservation(roomId, reservationDto);
        LOGGER.info("The following object was returned: " + reservationDtoOptional);

        return reservationDtoOptional
                .map(reservationEntity -> new ResponseEntity<>(reservationDtoOptional.get(), HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity("Room or person not found", HttpStatus.NOT_FOUND));
    }

}