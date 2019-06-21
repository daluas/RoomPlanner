package edu.roomplanner.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.roomplanner.entity.RoomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@JsonSerialize
@Setter
@Getter
public class FloorDto {

    private Long id;
    private Integer floor;
    private Set<RoomEntity> rooms;

}
