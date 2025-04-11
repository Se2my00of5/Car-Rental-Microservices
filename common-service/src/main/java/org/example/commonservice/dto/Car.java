package org.example.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    private Long id;

    private String brand;

    private String model;

    private String color;

    private int year;

    //regexp = "^[A-Z]{1}\\d{3}[A-Z]{2}\\d{2,3}$",
    //message = "Номер автомобиля должен быть в формате A123BC77"
    private String licensePlateNumber;

    private CarStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String userEmail;
}