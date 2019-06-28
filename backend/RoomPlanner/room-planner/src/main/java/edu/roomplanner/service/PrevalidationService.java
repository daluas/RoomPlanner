package edu.roomplanner.service;

import org.springframework.http.HttpStatus;

import java.util.Calendar;

public interface PrevalidationService {

    HttpStatus prevalidate(Calendar startDate, Calendar endDate, String email, Long roomId);
}
