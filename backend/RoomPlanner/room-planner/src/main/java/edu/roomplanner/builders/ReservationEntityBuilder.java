package edu.roomplanner.builders;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;

import java.util.Calendar;

public class ReservationEntityBuilder {

    private Long id;
    private Calendar startDate;
    private Calendar endDate;
    private String description;
    private UserEntity person;
    private UserEntity room;

    public static ReservationEntityBuilder builder() {
        return new ReservationEntityBuilder();
    }

    public ReservationEntityBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ReservationEntityBuilder withStartDate(Calendar startDate) {
        this.startDate = startDate;
        return this;
    }

    public ReservationEntityBuilder withEndDate(Calendar endDate) {
        this.endDate = endDate;
        return this;
    }

    public ReservationEntityBuilder withPerson(UserEntity person) {
        this.person = person;
        return this;
    }

    public ReservationEntityBuilder withRoom(UserEntity room) {
        this.room = room;
        return this;
    }

    public ReservationEntityBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ReservationEntity build() {

        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setId(id);
        reservationEntity.setStartDate(startDate);
        reservationEntity.setEndDate(endDate);
        reservationEntity.setPerson(person);
        reservationEntity.setRoom(room);
        reservationEntity.setDescription(description);

        return reservationEntity;
    }
}
