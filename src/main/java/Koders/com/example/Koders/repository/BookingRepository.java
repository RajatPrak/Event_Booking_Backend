package Koders.com.example.Koders.repository;

import Koders.com.example.Koders.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByBookingCode(String bookingCode);
    boolean existsByUser_IdAndEvent_EventId(Long userId, Long eventId);
    Optional<Booking> findByUser_IdAndEvent_EventId(Long userId, Long eventId);

    // ADD THIS
    List<Booking> findByUser_EmailOrderByCreatedAtDesc(String email);
}


