package Koders.com.example.Koders.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingResponse(
        String bookingCode,
        Long eventId,
        String eventTitle,
        String eventDescription,
        String eventCategory,
        LocalDate eventDate,
        LocalTime eventTime,
        String eventLocation
) {}
