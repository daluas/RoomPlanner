package edu.roomplanner.builders;

import edu.roomplanner.dto.ReservationDto;

import java.util.Calendar;
import java.util.Date;

public class ReservationDtoBuilder {
    private Long id;
    private Long roomId;
    private String email;
    private Calendar startDate;
    private Calendar endDate;
    private String description;

    public ReservationDtoBuilder withId(Long id){
        this.id=id;
        return this;
    }
    public ReservationDtoBuilder withRoomId(Long roomId){
        this.roomId=roomId;
        return this;
    }
    public ReservationDtoBuilder withEmail(String email){
        this.email=email;
        return this;
    }
    public ReservationDtoBuilder withStartDate(Calendar startDate){
        this.startDate =startDate;
        return this;
    }
    public ReservationDtoBuilder withEndDate(Calendar endDate){
        this.endDate=endDate;
        return this;
    }
    public ReservationDtoBuilder withDescription(String description){
        this.description=description;
        return this;
    }
    public ReservationDto build(){
        ReservationDto reservationDto=new ReservationDto();
        reservationDto.setId(id);
        reservationDto.setRoomId(roomId);
        reservationDto.setEmail(email);
        reservationDto.setStartDate(startDate);
        reservationDto.setEndDate(endDate);
        reservationDto.setDescription(description);

        return reservationDto;
    }
}
