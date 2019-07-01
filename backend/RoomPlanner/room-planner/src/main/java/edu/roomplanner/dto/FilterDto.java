package edu.roomplanner.dto;

import lombok.Data;

import java.util.Calendar;

@Data
public class FilterDto {
    Calendar startDate;
    Calendar endDate;
    Integer minPersons;
    Integer floor;
}
