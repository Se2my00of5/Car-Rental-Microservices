package org.example.carservice.service;

import org.example.carservice.dto.CarDTO;
import org.example.carservice.exception.NotFoundException;
import org.example.carservice.model.Car;
import org.example.carservice.model.CarStatus;
import org.example.carservice.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public CarDTO.Response.FullInfo createCar(CarDTO.Request.Create request) {
        Car car = Car.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .color(request.getColor())
                .year(request.getYear())
                .status(request.getStatus())
                .userEmail(request.getUserEmail())
                .createdAt(LocalDateTime.now())
                .build();

        car = carRepository.save(car);
        return toDto(car);
    }

    public CarDTO.Response.FullInfo updateCar(Long id, CarDTO.Request.Update request) {
        Car car = getCarOrThrow(id);

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setColor(request.getColor());
        car.setYear(request.getYear());
        car.setStatus(request.getStatus());

        car = carRepository.save(car);
        return toDto(car);
    }

    public List<CarDTO.Response.FullInfo> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::toDto)
                .collect(toList());
    }

    public List<CarDTO.Response.FullInfo> getAvailableCars() {
        return carRepository.findByStatus(CarStatus.AVAILABLE).stream()
                .map(this::toDto)
                .collect(toList());
    }

    public CarDTO.Response.FullInfo getCarById(Long id) {
        Car car = getCarOrThrow(id);
        return toDto(car);
    }

    public CarDTO.Response.FullInfo markAsInRepair(Long id) {
        Car car = getCarOrThrow(id);
        car.setStatus(CarStatus.IN_REPAIR);
        car = carRepository.save(car);
        return toDto(car);
    }

    public CarDTO.Response.FullInfo markAsAvailable(Long id) {
        Car car = getCarOrThrow(id);
        car.setStatus(CarStatus.AVAILABLE);
        car = carRepository.save(car);
        return toDto(car);
    }

    // ==================== Вспомогательные методы ====================

    private Car getCarOrThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Car not found"));
    }

    private CarDTO.Response.FullInfo toDto(Car car) {
        return new CarDTO.Response.FullInfo(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getColor(),
                car.getYear(),
                car.getStatus(),
                car.getUserEmail(),
                car.getCreatedAt(),
                car.getUpdatedAt()
        );
    }
}
