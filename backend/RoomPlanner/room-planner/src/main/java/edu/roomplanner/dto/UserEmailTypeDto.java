package edu.roomplanner.dto;

import edu.roomplanner.types.UserType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEmailTypeDto {

    private String email;
    private UserType type;

}
