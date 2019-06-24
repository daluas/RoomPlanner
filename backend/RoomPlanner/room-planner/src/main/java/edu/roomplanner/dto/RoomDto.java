package edu.roomplanner.dto;

import lombok.Data;


@Data
public class RoomDto extends UserDto {

    private String name;
    private FloorDto floor;
    private Integer maxPersons;

}
