package org.example.bookingservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long carId;

    @NotNull
    @Column(nullable = false)
    private Long userId;

    @NotNull
    @Column(nullable = false)
    private Long paymentId;

    @NotNull
    @Column(nullable = false)
    private boolean paymentConfirmed;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @Future
    @Column(nullable = false)
    private LocalDateTime endAt;

    private LocalDateTime endedAt;

}