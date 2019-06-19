package edu.roomplanner.dto;

import lombok.*;


@Data
public class RoomDto extends UserDto{

    private String name;
    private Integer floor;
    private Integer maxPersons;

}
