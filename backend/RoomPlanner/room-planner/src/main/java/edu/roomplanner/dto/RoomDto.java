package edu.roomplanner.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@JsonSerialize
@Setter
@Getter
@EqualsAndHashCode
public class RoomDto {
    private Long id;
    private String name;
    private Integer floor;
    private Integer maxPersons;
}
