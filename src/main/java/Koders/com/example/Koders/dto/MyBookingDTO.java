package Koders.com.example.Koders.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record MyBookingDTO(
        String bookingCode,
        Long eventId,
        String eventTitle,
        String eventDescription,
        String eventCategory,
        LocalDate eventDate,
        LocalTime eventTime,
        String eventLocation,
        LocalDateTime bookedAt
) {}
