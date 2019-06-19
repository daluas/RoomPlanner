package edu.roomplanner.rest;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.service.BookRoomService;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        LOGGER.info("Method was called.");
        ReservationEntity reservationEntity=bookRoomService.convertToEntity(reservationDto);
        ReservationEntity postCreated= bookRoomService.createReservation(reservationEntity);
        ReservationDto reservationDtoReturned = bookRoomService.convertToDto(postCreated);
        LOGGER.info("The following object was returned: " + reservationDtoReturned);
        if (reservationDto == null) {
            return new ResponseEntity<>(new ReservationDto(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        return new ResponseEntity<>(reservationDtoReturned, HttpStatus.CREATED);
    }

}
/*
@RequestMapping(method = RequestMethod.POST, value = "/reservations/{room_id}")
    ResponseEntity<ReservationEntity> getReservationCreated(@PathVariable(name = "room_id") Long roomId,
                                                            @RequestBody ReservationDto reservationDto) {

        LOGGER.info("Method was called.");
        ReservationEntity reservationEntity = bookRoomService.createReservation(roomId);
        LOGGER.info("The following object was returned: " + reservationEntity);
        if (reservationEntity == null) {
            return new ResponseEntity<>(new ReservationEntity(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        return new ResponseEntity<>(reservationEntity, HttpStatus.CREATED);
    }
 */
