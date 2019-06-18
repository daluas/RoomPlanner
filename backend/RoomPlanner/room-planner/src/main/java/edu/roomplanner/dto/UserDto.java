package edu.roomplanner.dto;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoleEntity;
import edu.roomplanner.types.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public abstract class UserDto {
    private Long id;
    private String email;
    private String password;
    private UserType type;
    private List<RoleEntity> roles;
    private Set<ReservationEntity> reservations;

}
