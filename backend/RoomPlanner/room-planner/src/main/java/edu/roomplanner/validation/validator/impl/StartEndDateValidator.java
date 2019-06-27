package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Component
@Order(3)
public class StartEndDateValidator implements BookingValidator {

    @Override
    public ValidationResult validate(ReservationEntity reservationEntity) {
        Calendar startDate = reservationEntity.getStartDate();
        if (getMinutesInFuture(startDate) < 0) {
            return new ValidationResult("Start date is in past!");
        }
        Calendar endDate = reservationEntity.getEndDate();
        if (getMinutesReservationTime(startDate, endDate) < MIN_MINUTES) {
            return new ValidationResult("End date is in past!");
        }
        return new ValidationResult();
    }

    private Long getMinutesInFuture(Calendar startDate) {
        return durationBetween(getSysDate(), startDate);
    }

    private Long getMinutesReservationTime(Calendar startDate, Calendar endDate) {
        return durationBetween(startDate, endDate);
    }

    private Calendar getSysDate() {
        Calendar sysDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        sysDate.setTime(conversionToGmt(sysDate.getTime()));
        return sysDate;
    }

    private Long durationBetween(Calendar startDate, Calendar endDate) {
        return TimeUnit.MINUTES.convert(endDate.getTime().getTime() - startDate.getTime().getTime(), TimeUnit.MILLISECONDS);
    }

    private Date conversionToGmt(Date date) {
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date(date.getTime() - tz.getRawOffset());
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }
}
