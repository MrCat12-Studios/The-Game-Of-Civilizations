package com.mrcat.civilizations.lifecycle;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.ZoneId;

public class Clock {

    public String getLocalDate() {
        LocalDate date = LocalDate.now();
        return date.toString();
    }
    
    public String getLocalTime(int digits) {
        String time = LocalTime.now(ZoneId.of("Europe/Athens")).toString();
        return time.substring(0, time.length() - digits);
    }
}