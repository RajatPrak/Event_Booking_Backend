package Koders.com.example.Koders.controller;
// Koders.com.example.Koders.dto.EventRequest

import Koders.com.example.Koders.dto.BookingResponse;
import Koders.com.example.Koders.dto.CancelBookingResponse;
import Koders.com.example.Koders.dto.EventRequest;
import Koders.com.example.Koders.model.Booking;
import Koders.com.example.Koders.model.Event;
import Koders.com.example.Koders.service.BookingService;
import Koders.com.example.Koders.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody EventRequest req) {
        Event created = eventService.create(req);
        return ResponseEntity.status(CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id,
                                             @Valid @RequestBody EventRequest req) {
        return ResponseEntity.ok(eventService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/book")
    public ResponseEntity<BookingResponse> bookEvent(@PathVariable Long id, Authentication auth) {
        String userEmail = (String) auth.getPrincipal();
        Booking booking = bookingService.bookEvent(userEmail, id);
        Event e = booking.getEvent();
        BookingResponse res = new BookingResponse(
                booking.getBookingCode(),
                e.getEventId(),
                e.getEventTitle(),
                e.getEventDescription(),
                e.getEventCategory(),
                e.getEventDate(),
                e.getEventTime(),
                e.getEventLocation()
        );
        return ResponseEntity.ok(res);
    }

    // ADD THIS
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelBookingResponse> cancelBooking(@PathVariable Long id, Authentication auth) {
        String userEmail = (String) auth.getPrincipal();
        CancelBookingResponse res = bookingService.cancelBooking(userEmail, id);
        return ResponseEntity.ok(res);
    }
}
