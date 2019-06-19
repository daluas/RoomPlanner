package edu.roomplanner.validation;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingChain {


    private List<BookingValidator> validatorChain;

    @Autowired
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
