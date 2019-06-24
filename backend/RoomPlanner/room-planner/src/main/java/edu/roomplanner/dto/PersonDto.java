package edu.roomplanner.dto;

import lombok.Data;


@Data
public class PersonDto extends UserDto {

    private String firstName;
    private String lastName;

}
