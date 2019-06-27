package edu.roomplanner.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


import java.util.Calendar;
import java.util.Date;


@JsonSerialize
@Getter
@Setter
@EqualsAndHashCode
@Component
public class ReservationDto {

    private Long id;
    private Long roomId;
    private String email;
    private Calendar startDate;
    private Calendar endDate;
    private String description;

}
