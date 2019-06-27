package edu.roomplanner.builders;

import edu.roomplanner.dto.ReservationDto;

import java.util.Calendar;

public class ReservationDtoBuilder {

    private Long id;
    private Long roomId;
    private String personEmail;
    private Calendar startDate;
    private Calendar endDate;
    private String description;

    public static ReservationDtoBuilder builder() {
        return new ReservationDtoBuilder();
    }

    public ReservationDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ReservationDtoBuilder withStartDate(Calendar startDate) {
        this.startDate = startDate;
        return this;
    }

    public ReservationDtoBuilder withEndDate(Calendar endDate) {
        this.endDate = endDate;
        return this;
    }


    public ReservationDtoBuilder withRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }

    public ReservationDtoBuilder withPersonEmail(String personEmail) {
        this.personEmail = personEmail;
        return this;
    }

    public ReservationDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ReservationDto build() {

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(id);
        reservationDto.setRoomId(roomId);
        reservationDto.setStartDate(startDate);
        reservationDto.setEndDate(endDate);
        reservationDto.setPersonEmail(personEmail);
        reservationDto.setDescription(description);

        return reservationDto;
    }
}
