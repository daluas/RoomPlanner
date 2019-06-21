package edu.roomplanner.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@JsonSerialize
@Setter
@Getter
@EqualsAndHashCode
public class FloorDto {

    private Long id;
    private Integer floor;
    private Set<RoomDto> rooms;

}
