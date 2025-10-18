package Koders.com.example.Koders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventRequest(
        @NotBlank String eventTitle,
        @NotBlank String eventDescription,
        @NotBlank String eventCategory,
        LocalDate eventDate,
        LocalTime eventTime,
        @Min(1) Integer totalSeats,
        @NotBlank String eventLocation
) {}
