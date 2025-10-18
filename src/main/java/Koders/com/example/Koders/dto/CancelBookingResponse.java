package Koders.com.example.Koders.dto;

public record CancelBookingResponse(
        String bookingCode,
        Long eventId,
        String eventTitle,
        String eventDescription,
        int bookedSeats,
        String message
) {}
