package edu.roomplanner.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonSerialize
@Setter
@Getter
@EqualsAndHashCode
public class RoomDto{
    private Long id;
    private String name;
    private Integer floor;
    private Integer maxPersons;
    private List<ReservationDto> reservation;
}
