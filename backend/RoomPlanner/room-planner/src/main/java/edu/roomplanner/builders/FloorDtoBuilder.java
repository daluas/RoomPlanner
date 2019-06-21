package edu.roomplanner.builders;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.entity.RoomEntity;

import java.util.Set;

public class FloorDtoBuilder {

    private Long id;
    private Integer floor;
    private Set<RoomEntity> rooms;


    public FloorDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public FloorDtoBuilder withFloor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public FloorDtoBuilder withRooms(Set<RoomEntity> rooms) {
        this.rooms = rooms;
        return this;
    }

    public FloorDto build() {

        FloorDto floorBuilded = new FloorDto();
        floorBuilded.setId(id);
        floorBuilded.setFloor(floor);
        floorBuilded.setRooms(rooms);

        return floorBuilded;
    }
}
