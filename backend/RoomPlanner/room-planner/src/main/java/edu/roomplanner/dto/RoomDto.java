package edu.roomplanner.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoomDto extends UserDto {

    private String name;
    private Integer floor;
    private Integer maxPersons;

}
