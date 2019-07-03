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

@RestController
@Api(value = "Reservation API", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationRestController {

    private static final Logger LOGGER = LogManager.getLogger(ReservationRestController.class);

    private final ReservationService bookRoomService;

    @Autowired
    public ReservationRestController(ReservationService bookRoomService) {
        this.bookRoomService = bookRoomService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/reservations/{room_id}", produces = "application/json")
    @ApiOperation("Books a reservation for a room with a specific id.")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Reservation was booked successfully."),
            @ApiResponse(code = 404, message = "This room was not found."),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    @PreAuthorize("hasAuthority('person')")
    public ResponseEntity<ReservationDto> postReservationCreated(@PathVariable(name = "room_id") Long roomId,
                                                          @RequestBody ReservationDto reservationDto) {

        LOGGER.info("Method was called.");
        ReservationDto reservationDtoResult = bookRoomService.createReservation(roomId, reservationDto);
        LOGGER.info("The following object was returned: " + reservationDtoResult);

        return new ResponseEntity<>(reservationDtoResult, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/reservations")
    @ApiOperation("Delete a reservation for a room with a specific id.")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Not a valid reservation id."),
            @ApiResponse(code = 200, message = "Reservation was deleted."),
            @ApiResponse(code = 401, message = "User want to delete a reservation from another user.")})
    @PreAuthorize("hasAuthority('person')")
    ResponseEntity<HttpStatus> deleteReservation(@RequestParam("reservation") Long reservationId) {
        LOGGER.info("Method was called.");

        bookRoomService.deleteReservation(reservationId);
        LOGGER.info("Exist method called");
        return new ResponseEntity<>(HttpStatus.OK);

    }

}