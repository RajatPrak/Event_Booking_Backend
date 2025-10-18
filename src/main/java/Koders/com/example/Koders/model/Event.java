package Koders.com.example.Koders.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @NotBlank
    @Column(name = "event_title")
    private String eventTitle;

    @NotBlank
    @Column(name = "event_description",columnDefinition = "text")
    private String eventDescription;

    @NotBlank
    @Column(name = "event_category")
    private String eventCategory;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "event_time")
    private LocalTime eventTime;

    @Min(1)
    @Column(name = "total_seats")
    private Integer totalSeats;

    @Min(0)
    @Column(name = "booked_seats")
    private Integer bookedSeats;

    @NotBlank
    @Column(name = "event_location")
    private String eventLocation;
}

