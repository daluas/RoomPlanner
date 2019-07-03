package edu.roomplanner.mappers.impl;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReservationDtoMapperImpl implements ReservationDtoMapper {

    private UserRepository userRepository;

    @Autowired
    public ReservationDtoMapperImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ReservationDto mapReservationEntityToDto(ReservationEntity reservationEntity) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservationEntity.getId());
        reservationDto.setRoomId(reservationEntity.getRoom().getId());
        reservationDto.setRoomId(reservationEntity.getRoom().getId());
        reservationDto.setEmail(reservationEntity.getPerson().getEmail());
        reservationEntity.getStartDate().setTime(reservationEntity.getStartDate().getTime());
        reservationDto.setStartDate(reservationEntity.getStartDate());
        reservationEntity.getEndDate().setTime(reservationEntity.getEndDate().getTime());
        reservationDto.setEndDate(reservationEntity.getEndDate());
        reservationDto.setDescription(reservationEntity.getDescription());
        return reservationDto;
    }

    public ReservationEntity mapReservationDtoToEntity(ReservationDto reservationDto) {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setId(reservationDto.getId());
        reservationDto.getStartDate().setTime(reservationDto.getStartDate().getTime());
        reservationEntity.setStartDate(reservationDto.getStartDate());
        reservationDto.getEndDate().setTime(reservationDto.getEndDate().getTime());
        reservationEntity.setEndDate(reservationDto.getEndDate());
        reservationEntity.setDescription(reservationDto.getDescription());
        Optional<UserEntity> roomEntityOptional = userRepository.findById(reservationDto.getRoomId());
        roomEntityOptional.ifPresent(reservationEntity::setRoom);
        Optional<UserEntity> userEntity = userRepository.findByEmail(reservationDto.getEmail());
        userEntity.ifPresent(reservationEntity::setPerson);

        return reservationEntity;
    }

}
