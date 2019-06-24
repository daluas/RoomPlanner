package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component("Validator")
@Order(4)
public class AvailabilityValidator implements BookingValidator {

    private ReservationRepository reservationRepository;

    @Autowired
    public AvailabilityValidator(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ValidationResult validate(ReservationEntity reservationEntity) {
        Calendar startDate = reservationEntity.getStartDate();
        Calendar endDate = reservationEntity.getEndDate();
        UserEntity room = reservationEntity.getRoom();

        startDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE) + 1);
        endDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE) - 1);

        List<ReservationEntity> nonAvailableReservations = reservationRepository.findNonAvailableDate(startDate, endDate, room.getId());

        Boolean areNonAvailableReservationForSameUser = areNonAvailableReservationForSameUser(nonAvailableReservations, reservationEntity);
        
        if (areNonAvailableReservationForSameUser) {
            return new ValidationResult();
        }
        return new ValidationResult("The date is not available!");

    }


    private Boolean areNonAvailableReservationForSameUser(List<ReservationEntity> nonAvailableReservations,
                                                        ReservationEntity currentReservationEntity) {
        for(ReservationEntity nonAvailableReservation:nonAvailableReservations) {
            Long nonAvailableReservationPersonId = nonAvailableReservation.getPerson().getId();
            Long currentReservationPersonId = currentReservationEntity.getPerson().getId();
            if(!nonAvailableReservationPersonId.equals(currentReservationPersonId)) {
                return false;
            }
        }
        return true;
    }
}
