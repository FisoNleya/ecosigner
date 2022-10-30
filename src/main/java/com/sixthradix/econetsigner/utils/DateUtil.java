package com.sixthradix.econetsigner.utils;

import com.sixthradix.econetsigner.exceptions.ApplicationRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class DateUtil {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public LocalDateTime stringToDate(String dateString){
        LocalDateTime dateTime = null;

        try{
            dateTime = LocalDateTime.parse(dateString.concat(" 00:00"), DATE_TIME_FORMATTER);
        }catch (Exception e){
             throw new ApplicationRequestException("Invalid date format");
        }
        return  dateTime;
    }

    public void validateDate(LocalDateTime startDate, LocalDateTime endDate) {

        if (endDate.isBefore(startDate))
            throw new ApplicationRequestException("End Date cannot be less than Start Date");
    }

}
