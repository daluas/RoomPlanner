package edu.roomplanner.builders;

import edu.roomplanner.dto.PersonDto;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.exception.UnknownUserTypeException;
import edu.roomplanner.types.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class UserDtoBuilder {

    private Long id;
    private String email;
    private String name;
    private Integer floor;
    private Integer maxPersons;
    private UserType type;
    private String firstName;
    private String lastName;
    private Set<ReservationDto> reservations;
    private static Logger LOGGER = LogManager.getLogger(UserDtoBuilder.class);

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public UserDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserDtoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserDtoBuilder withFloor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public UserDtoBuilder withMaxPersons(Integer maxPersons) {
        this.maxPersons = maxPersons;
        return this;
    }

    public UserDtoBuilder withType(UserType type) {
        this.type = type;
        return this;
    }

    public UserDtoBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserDtoBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserDtoBuilder withReservations(Set<ReservationDto> reservations) {
        this.reservations = reservations;
        return this;
    }

    public UserDto build() {
        switch (type) {
            case ROOM: {
                return buildRoomDto();
            }
            case PERSON: {
                return buildPersonDto();
            }
            default: {
                LOGGER.error("Unknown user type: " + type);
                throw new UnknownUserTypeException("Unknown user type: " + type);
            }
        }
    }

    private RoomDto buildRoomDto() {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(id);
        roomDto.setEmail(email);
        roomDto.setType(type);
        roomDto.setName(name);
        roomDto.setFloor(floor);
        roomDto.setMaxPersons(maxPersons);
        roomDto.setReservations(reservations);
        return roomDto;
    }

    private PersonDto buildPersonDto() {
        PersonDto personDto = new PersonDto();
        personDto.setId(id);
        personDto.setEmail(email);
        personDto.setType(type);
        personDto.setFirstName(firstName);
        personDto.setLastName(lastName);
        personDto.setReservations(reservations);
        return personDto;
    }
}
