package edu.roomplanner.validation;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BookingChain {

    @Autowired
    private List<BookingValidator> validatorChain;

    public BookingChain(List<BookingValidator> validatorChain) {
        this.validatorChain = validatorChain;
    }

    public List<String> validate(ReservationEntity reservationEntity) {
        List<String> errors = new ArrayList<>();
        validatorChain.stream()
                .map(validator -> validator.validate(reservationEntity))
                .filter(ValidationResult::isError)
                .map(ValidationResult::getError)
                .forEach(errors::add);
        return errors;
    }


}
