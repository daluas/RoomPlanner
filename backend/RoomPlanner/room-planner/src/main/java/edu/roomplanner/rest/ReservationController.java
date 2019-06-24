package edu.roomplanner.rest;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.service.ReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ReservationController {

    private static final Logger LOGGER = LogManager.getLogger(ReservationController.class);

    private final ReservationService bookRoomService;

    @Autowired
    public ReservationController(ReservationService bookRoomService) {
        this.bookRoomService = bookRoomService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/reservations/{room_id}")
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