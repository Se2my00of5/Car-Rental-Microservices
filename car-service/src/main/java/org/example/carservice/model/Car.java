package org.example.carservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.commonservice.dto.CarStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String brand;

    @NotBlank
    @Column(nullable = false)
    private String model;

    @NotBlank
    @Column(nullable = false)
    private String color;

    @Min(1900)
    @Max(2100)
    @Column(nullable = false)
    private int year;

    @Pattern(
            regexp = "^[A-Z]{1}\\d{3}[A-Z]{2}\\d{2,3}$",
            message = "Номер автомобиля должен быть в формате A123BC77"
    )
    @Column(nullable = false, unique = true, length = 15)
    private String licensePlateNumber;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String userEmail;



}