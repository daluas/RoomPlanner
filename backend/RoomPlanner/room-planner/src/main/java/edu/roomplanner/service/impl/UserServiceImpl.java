package edu.roomplanner.service.impl;

import edu.roomplanner.dto.UserDto;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.UserService;
import edu.roomplanner.types.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> getUsers() {
        return (List<UserEntity>) userRepository.findAll();
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<UserDto> getUserDto(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if(userEntity.isPresent()){
            UserDto userDto = buildUserDto(userEntity.get());
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    private UserDto buildUserDto(UserEntity userEntity) {
        if(userEntity.getType() == UserType.PERSON) {
            return buildUserDtoByPersonEntity((PersonEntity) userEntity);
        }
        return buildUserDtoByRoomEntity((RoomEntity) userEntity);
    }

    private UserDto buildUserDtoByPersonEntity(PersonEntity personEntity) {
        return UserDto.builder()
                .id(personEntity.getId())
                .email(personEntity.getEmail())
                .type(personEntity.getType())
                .firstName(personEntity.getFirstName())
                .lastName(personEntity.getLastName())
                .build();
    }

    private UserDto buildUserDtoByRoomEntity(RoomEntity roomEntity) {
        return UserDto.builder()
                .id(roomEntity.getId())
                .email(roomEntity.getEmail())
                .type(roomEntity.getType())
                .roomName(roomEntity.getName())
                .floor(roomEntity.getFloor())
                .maxPersons(roomEntity.getMaxPersons())
                .build();
    }

}
