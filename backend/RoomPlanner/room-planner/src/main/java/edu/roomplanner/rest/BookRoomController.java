package edu.roomplanner.rest;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.service.BookRoomService;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@EqualsAndHashCode
@RestController
public class BookRoomController {

    private static Logger LOGGER = LogManager.getLogger(BookRoomController.class);

    private final BookRoomService bookRoomService;

    @Autowired
    public BookRoomController(BookRoomService bookRoomService) {
        this.bookRoomService = bookRoomService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reservations/{room_id}")
    ResponseEntity<ReservationDto> getReservationCreated(@PathVariable(name = "room_id") Long roomId,
                                                         @RequestBody ReservationDto reservationDto) {
        if (reservationDto == null) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }

        LOGGER.info("Method was called.");
        Optional<ReservationDto> reservationDtoOptional = bookRoomService.createReservation(roomId, reservationDto);
        LOGGER.info("The following object was returned: " + reservationDtoOptional);

        return reservationDtoOptional
                .map(reservationEntity -> new ResponseEntity<>(reservationDtoOptional.get(), HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}