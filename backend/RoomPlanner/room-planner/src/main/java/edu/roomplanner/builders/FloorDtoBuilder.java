package edu.roomplanner.builders;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.dto.RoomDto;

import java.util.Set;

public class FloorDtoBuilder {

    private Long id;
    private Integer floor;
    private Set<RoomDto> rooms;

    public static FloorDtoBuilder builder() {
        return new FloorDtoBuilder();
    }

    public FloorDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public FloorDtoBuilder withFloor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public FloorDtoBuilder withRooms(Set<RoomDto> rooms) {
        this.rooms = rooms;
        return this;
    }

    public FloorDto build() {

        FloorDto floorBuilt = new FloorDto();
        floorBuilt.setId(id);
        floorBuilt.setFloor(floor);
        floorBuilt.setRooms(rooms);

        return floorBuilt;
    }

}
