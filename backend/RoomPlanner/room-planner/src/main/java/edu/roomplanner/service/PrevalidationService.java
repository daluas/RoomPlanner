package edu.roomplanner.service;

import java.util.Calendar;

public interface PrevalidationService {

    String prevalidate(Calendar startDate, Calendar endDate, String email, Long roomId);
}
