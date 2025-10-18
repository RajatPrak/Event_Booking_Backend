package Koders.com.example.Koders.repository;

import Koders.com.example.Koders.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
