package Koders.com.example.Koders.service;

import Koders.com.example.Koders.dto.CancelBookingResponse;
import Koders.com.example.Koders.dto.MyBookingDTO;
import Koders.com.example.Koders.dto.MyBookingsResponse;
import Koders.com.example.Koders.model.Booking;
import Koders.com.example.Koders.model.Event;
import Koders.com.example.Koders.model.User;
import Koders.com.example.Koders.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserService userService;
    private final EventService eventService;
    private final BookingRepository bookingRepository;
    private final BookingCodeGenerator codeGenerator;

    @Transactional
    public Booking bookEvent(String userEmail, Long eventId) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        Event event = eventService.getById(eventId);

        if (event.getBookedSeats() >= event.getTotalSeats()) {
            throw new ResponseStatusException(BAD_REQUEST, "No seats available");
        }

        boolean alreadyBooked = user.getAllBookedEvents().stream()
                .anyMatch(e -> e.getEventId().equals(eventId));
        if (alreadyBooked || bookingRepository.existsByUser_IdAndEvent_EventId(user.getId(), eventId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Event already booked by user");
        }

        event.setBookedSeats(event.getBookedSeats() + 1);
        user.getAllBookedEvents().add(event);

        // Generate unique booking code
        String code;
        int attempts = 0;
        do {
            code = codeGenerator.generate(event.getEventDate());
            attempts++;
            if (attempts > 10) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Could not generate a unique booking code, please retry");
            }
        } while (bookingRepository.existsByBookingCode(code));

        Booking booking = Booking.builder()
                .bookingCode(code)
                .user(user)
                .event(event)
                .createdAt(LocalDateTime.now())
                .build();

        bookingRepository.save(booking);
        eventService.save(event);
        userService.save(user);

        return booking;
    }

    // ADD THIS
    @Transactional
    public CancelBookingResponse cancelBooking(String userEmail, Long eventId) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        Event event = eventService.getById(eventId);

        Booking booking = bookingRepository.findByUser_IdAndEvent_EventId(user.getId(), eventId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No booking found for this event"));

        // Remove the event from the user's booked list (by ID to avoid proxy equality issues)
        user.getAllBookedEvents().removeIf(e -> e.getEventId().equals(eventId));

        // Decrement seats defensively (no negative)
        int bs = event.getBookedSeats() == null ? 0 : event.getBookedSeats();
        event.setBookedSeats(bs > 0 ? bs - 1 : 0);

        // Delete booking
        String code = booking.getBookingCode();
        bookingRepository.delete(booking);

        // Persist changes
        eventService.save(event);
        userService.save(user);

        return new CancelBookingResponse(
                code,
                event.getEventId(),
                event.getEventTitle(),
                event.getEventDescription(),
                event.getBookedSeats(),
                "Booking cancelled successfully"
        );
    }


    // ADD THIS
    @Transactional(readOnly = true)
    public MyBookingsResponse myBookings(String userEmail) {
        List<Booking> bookings = bookingRepository.findByUser_EmailOrderByCreatedAtDesc(userEmail);

        List<MyBookingDTO> items = bookings.stream().map(b -> {
            Event e = b.getEvent();
            return new MyBookingDTO(
                    b.getBookingCode(),
                    e.getEventId(),
                    e.getEventTitle(),
                    e.getEventDescription(),
                    e.getEventCategory(),
                    e.getEventDate(),
                    e.getEventTime(),
                    e.getEventLocation(),
                    b.getCreatedAt()
            );
        }).toList();

        return new MyBookingsResponse(items.size(), items);
    }

}
