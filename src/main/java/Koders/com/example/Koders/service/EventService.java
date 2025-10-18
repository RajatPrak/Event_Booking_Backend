package Koders.com.example.Koders.service;

import Koders.com.example.Koders.dto.EventRequest;
import Koders.com.example.Koders.model.Event;
import Koders.com.example.Koders.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Event create(EventRequest req) {
        Event e = Event.builder()
                .eventTitle(req.eventTitle())
                .eventDescription(req.eventDescription())
                .eventCategory(req.eventCategory())
                .eventDate(req.eventDate())
                .eventTime(req.eventTime())
                .totalSeats(req.totalSeats())
                .bookedSeats(0)
                .eventLocation(req.eventLocation())
                .build();
        return eventRepository.save(e);
    }

    public Event update(Long id, EventRequest req) {
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Event not found"));
        e.setEventTitle(req.eventTitle());
        e.setEventDescription(req.eventDescription());
        e.setEventCategory(req.eventCategory());
        e.setEventDate(req.eventDate());
        e.setEventTime(req.eventTime());
        e.setEventLocation(req.eventLocation());
        // Adjust total seats only if it doesn't violate current bookings
        if (req.totalSeats() != null) {
            if (req.totalSeats() < e.getBookedSeats()) {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                        "totalSeats cannot be less than bookedSeats");
            }
            e.setTotalSeats(req.totalSeats());
        }
        return eventRepository.save(e);
    }

    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Event not found");
        }
        eventRepository.deleteById(id);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Event not found"));
    }

    public Event save(Event e) {
        return eventRepository.save(e);
    }
}
