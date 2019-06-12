package edu.roomplanner.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize
@Setter
@Getter
@EqualsAndHashCode
public class RoomDto {
    private Long id;
    private String name;
    private Integer floor;
    private Integer maxPersons;
}
