package edu.roomplanner.util;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;

import java.util.Calendar;

public class ReservationEntityUtil {

    public Calendar createDate(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(year, month, date, hourOfDay, minute, second);
        return calendarDate;
    }

    public ReservationEntity getReservationEntity(Long personId, Calendar startDate, Calendar endDate) {

        RoomEntity room = new RoomEntity();
        room.setId(1L);

        PersonEntity person = new PersonEntity();
        person.setId(personId);

        return new ReservationEntityBuilder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withRoom(room)
                .withPerson(person)
                .build();

    }

}
