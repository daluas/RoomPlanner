package edu.roomplanner.service.impl;

import edu.roomplanner.builders.UserDtoBuilder;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.exception.UserNotFoundException;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.TokenParserService;
import edu.roomplanner.service.UserService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.validator.UserValidator;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Builder
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserValidator userValidator;
    private RoomDtoMapper roomDtoMapper;
    private TokenParserService tokenParserService;
    private ReservationDtoMapper reservationDtoMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator,
                           RoomDtoMapper roomDtoMapper, TokenParserService tokenParserService,
                           ReservationDtoMapper reservationDtoMapper) {

        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.roomDtoMapper = roomDtoMapper;
        this.tokenParserService = tokenParserService;
        this.reservationDtoMapper = reservationDtoMapper;

    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<UserEntity> roomEntities = userRepository.findByType(UserType.ROOM);
        roomEntities = updateUserEntitiesReservation(roomEntities);
        return roomDtoMapper.mapEntityListToDtoList(roomEntities);
    }

    @Override
    public RoomDto getRoomById(Long id) {
        RoomDto roomDto = null;
        if (userValidator.checkValidRoomId(id)) {
            RoomEntity userEntity = (RoomEntity) userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            Set<ReservationEntity> updatedReservationEntities = updateReservationDescription(userEntity.getReservations());
            userEntity.setReservations(updatedReservationEntities);
            roomDto = roomDtoMapper.mapEntityToDto(userEntity);
        }
        return roomDto;
    }

    @Override
    public RoomDto getRoomByEmail(String email) {
        RoomDto roomDto = null;
        if (userValidator.checkValidRoomEmail(email)){
            UserEntity userEntity = userRepository.findByEmail(email).get();
            roomDto = roomDtoMapper.mapEntityToDto((RoomEntity) userEntity);
        }
        return roomDto;
    }

    @Override
    public Optional<UserDto> getUserDto(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            UserDto userDto = buildUserDto(userEntity.get());
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public Set<ReservationEntity> updateReservationDescription(Set<ReservationEntity> reservationEntities) {
        Long userId = getLoggedUserId();
        reservationEntities.stream()
                .filter(reservationEntity -> !reservationEntity.getPerson().getId().equals(userId))
                .forEach((reservationEntity) -> reservationEntity.setDescription(null));
        return reservationEntities;
    }

    @Override
    public List<UserEntity> updateUserEntitiesReservation(List<UserEntity> userEntities) {
        for (UserEntity userEntity : userEntities) {
            Set<ReservationEntity> updatedReservationEntities = updateReservationDescription(((RoomEntity) userEntity).getReservations());
            ((RoomEntity) userEntity).setReservations(updatedReservationEntities);
        }
        return userEntities;
    }

    private Long getLoggedUserId() {
        Optional<UserEntity> loggedPersonOptional = userRepository.findByEmail(tokenParserService.getEmailFromToken());
        return loggedPersonOptional
                .map(UserEntity::getId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private UserDto buildUserDto(UserEntity userEntity) {
        if (userEntity.getType() == UserType.PERSON) {
            return buildUserDtoByPersonEntity((PersonEntity) userEntity);
        }
        return buildUserDtoByRoomEntity((RoomEntity) userEntity);
    }

    private UserDto buildUserDtoByPersonEntity(PersonEntity personEntity) {
        return UserDtoBuilder.builder()
                .withId(personEntity.getId())
                .withEmail(personEntity.getEmail())
                .withType(personEntity.getType())
                .withReservations(getReservationDtos(personEntity.getReservations()))
                .withFirstName(personEntity.getFirstName())
                .withLastName(personEntity.getLastName())
                .build();
    }

    private UserDto buildUserDtoByRoomEntity(RoomEntity roomEntity) {

        return UserDtoBuilder.builder()
                .withId(roomEntity.getId())
                .withEmail(roomEntity.getEmail())
                .withType(roomEntity.getType())
                .withName(roomEntity.getName())
                .withReservations(getReservationDtos(roomEntity.getReservations()))
                .withFloor(roomEntity.getFloor().getFloor())
                .withMaxPersons(roomEntity.getMaxPersons())
                .build();
    }

    private Set<ReservationDto> getReservationDtos(Set<ReservationEntity> reservationEntities) {
        Set<ReservationDto> reservationDtos = new HashSet<>();
        for(ReservationEntity reservationEntity:reservationEntities) {
            reservationDtos.add(reservationDtoMapper.mapReservationEntityToDto(reservationEntity));
        }
        return reservationDtos;
    }

}
