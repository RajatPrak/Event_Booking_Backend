package Koders.com.example.Koders.dto;

import java.util.List;

public record MyBookingsResponse(
        int total,
        List<MyBookingDTO> bookings
) {}
