package edu.roomplanner.service;

import java.util.Calendar;

public interface PrevalidationService {

    Integer prevalidate(Calendar startDate, Calendar endDate, String email, Long roomId);
}
