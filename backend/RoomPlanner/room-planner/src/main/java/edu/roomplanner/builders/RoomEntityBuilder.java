package edu.roomplanner.builders;

import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;

public class RoomEntityBuilder {

    private Long id;
    private String email;
    private String password;
    private UserType type;
    private String name;
    private Integer floor;
    private Integer maxPersons;

    public RoomEntityBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RoomEntityBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public RoomEntityBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public RoomEntityBuilder withType(UserType type) {
        this.type = type;
        return this;
    }

    public RoomEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RoomEntityBuilder withFloor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public RoomEntityBuilder withMaxPersons(Integer maxPersons) {
        this.maxPersons = maxPersons;
        return this;
    }

    public UserEntity build() {
        UserEntity userEntity = new RoomEntity();
        userEntity.setId(id);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setType(type);
        userEntity.setId(id);
        ((RoomEntity) userEntity).setName(name);
        ((RoomEntity) userEntity).setFloor(floor);
        ((RoomEntity) userEntity).setMaxPersons(maxPersons);

        return userEntity;
    }
}
