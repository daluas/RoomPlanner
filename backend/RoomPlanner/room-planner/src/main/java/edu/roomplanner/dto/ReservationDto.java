package edu.roomplanner.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@JsonSerialize
@Getter
@Setter
@EqualsAndHashCode
@Component
public class ReservationDto {

    private Long id;
    private Long roomId;
    private String personEmail;
    private Calendar startDate;
    private Calendar endDate;
    private String description;

}
