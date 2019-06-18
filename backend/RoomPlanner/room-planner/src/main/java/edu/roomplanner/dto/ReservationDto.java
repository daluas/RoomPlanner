package edu.roomplanner.dto;

import edu.roomplanner.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class ReservationDto {
    private Long id;
    private Calendar startDate;
    private Calendar endDate;
    private String description;
    private UserEntity person;
}
