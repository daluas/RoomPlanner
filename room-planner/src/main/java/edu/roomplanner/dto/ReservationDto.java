package edu.roomplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private Long personId;
    private Calendar startDate;
    private Calendar endDate;
    private String description;


}
