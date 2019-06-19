package edu.roomplanner.builders;

import edu.roomplanner.dto.PersonDto;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.exception.UnknownUserTypeException;
import edu.roomplanner.types.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class UserEntityBuilder {

    private Long id;
    private String email;
    private String password;
    private UserType type;
    private String firstName;
    private String lastName;
    private String name;
    private Integer floor;
    private Integer maxPersons;
    private Set<ReservationEntity> reservations;
    private static Logger LOGGER = LogManager.getLogger(UserEntityBuilder.class);

    public static UserEntityBuilder builder() {
        return new UserEntityBuilder();
    }

    public UserEntityBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserEntityBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserEntityBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntityBuilder withType(UserType type) {
        this.type = type;
        return this;
    }

    public UserEntityBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserEntityBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserEntityBuilder withFloor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public UserEntityBuilder withMaxPersons(Integer maxPersons) {
        this.maxPersons = maxPersons;
        return this;
    }

    public UserEntityBuilder withReservations(Set<ReservationEntity> reservations) {
        this.reservations = reservations;
        return this;
    }

    public UserEntity build() {
        switch (type){
            case ROOM: {
                return buildRoomEntity();
            }
            case PERSON:{
                return buildPersonEntity();
            }
            default: {
                LOGGER.error("Unknown user type: " + type);
                throw new UnknownUserTypeException("Unknown user type: " + type);
            }
        }
    }

    private RoomEntity buildRoomEntity() {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(id);
        roomEntity.setEmail(email);
        roomEntity.setPassword(password);
        roomEntity.setType(type);
        roomEntity.setName(name);
        roomEntity.setFloor(floor);
        roomEntity.setMaxPersons(maxPersons);
        roomEntity.setReservations(reservations);
        return roomEntity;
    }

    private PersonEntity buildPersonEntity() {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(id);
        personEntity.setEmail(email);
        personEntity.setPassword(password);
        personEntity.setType(type);
        personEntity.setFirstName(firstName);
        personEntity.setLastName(lastName);
        personEntity.setReservations(reservations);
        return personEntity;
    }
}
