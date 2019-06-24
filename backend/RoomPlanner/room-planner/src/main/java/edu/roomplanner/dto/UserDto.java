package edu.roomplanner.dto;

import edu.roomplanner.types.UserType;
import lombok.Data;

import java.util.Set;


@Data
public abstract class UserDto {

    private Long id;
    private String email;
    private UserType type;
    private Set<ReservationDto> reservations;

}
