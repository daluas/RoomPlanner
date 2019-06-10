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

    public RoomDto() {
    }

    public static RoomDto mapToDto(RoomEntity roomEntity) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(roomEntity.getId());
        roomDto.setFloor(roomEntity.getFloor());
        roomDto.setName(roomEntity.getRoomName());
        roomDto.setMaxPersons(roomEntity.getMaxPersons());
        return roomDto;
    }

    public static List<RoomDto> mapListToDto(List<UserEntity> roomEntityList) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (UserEntity room : roomEntityList) {
            roomDtoList.add(mapToDto((RoomEntity) room));
        }
        return roomDtoList;
    }
}
