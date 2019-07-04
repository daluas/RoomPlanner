package edu.roomplanner.util;

import java.util.Date;
import java.util.TimeZone;

public class ConverterUtil {

    public static Date conversionToGmt(Date date) {
        TimeZone timeZone = TimeZone.getDefault();
        Date resultDate = new Date(date.getTime() - timeZone.getRawOffset());
        if (timeZone.inDaylightTime(resultDate)) {
            Date destinationDate = new Date(resultDate.getTime() - timeZone.getDSTSavings());
            if (timeZone.inDaylightTime(destinationDate)) {
                resultDate = destinationDate;
            }
        }
        return resultDate;
    }

}
