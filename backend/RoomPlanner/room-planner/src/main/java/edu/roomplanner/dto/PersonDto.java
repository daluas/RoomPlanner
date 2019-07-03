package edu.roomplanner.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PersonDto extends UserDto {

    private String firstName;
    private String lastName;

}
