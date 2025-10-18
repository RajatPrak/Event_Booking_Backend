package Koders.com.example.Koders.controller;

import Koders.com.example.Koders.dto.MyBookingsResponse;
import Koders.com.example.Koders.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final BookingService bookingService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/bookings")
    public ResponseEntity<MyBookingsResponse> myBookings(Authentication auth) {
        String userEmail = (String) auth.getPrincipal();
        return ResponseEntity.ok(bookingService.myBookings(userEmail));
    }
}
