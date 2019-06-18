package edu.roomplanner.dto;

import edu.roomplanner.types.UserType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long id;
    private String email;
    private UserType type;
    private String firstName;
    private String lastName;
    private String roomName;
    private Integer floor;
    private Integer maxPersons;

}
