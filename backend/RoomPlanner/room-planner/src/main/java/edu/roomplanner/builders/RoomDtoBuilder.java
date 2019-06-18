package edu.roomplanner.builders;

import edu.roomplanner.dto.RoomDto;

public class RoomDtoBuilder {

    private Long id;
    private String name;
    private Integer floor;
    private Integer maxPersons;

    public RoomDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RoomDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RoomDtoBuilder withFloor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public RoomDtoBuilder withMaxPersons(Integer maxPersons) {
        this.maxPersons = maxPersons;
        return this;
    }

    public RoomDto build() {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(id);
        roomDto.setName(name);
        roomDto.setFloor(floor);
        roomDto.setMaxPersons(maxPersons);
        return roomDto;
    }
}
