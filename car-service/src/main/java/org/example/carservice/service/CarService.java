package org.example.carservice.service;

import lombok.extern.slf4j.Slf4j;
import org.example.carservice.model.Car;
import org.example.carservice.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.example.commonservice.dto.CarDTO;
import org.example.commonservice.dto.CarStatus;
import org.example.commonservice.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final JwtProvider jwtProvider;

    public CarDTO.Response.FullInfo createCar(String authHeader, CarDTO.Request.Create request) {
        Car car = Car.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .color(request.getColor())
                .year(request.getYear())
                .status(CarStatus.AVAILABLE)
                .userEmail(jwtProvider.getEmailFromAuthHeader(authHeader))
                .licensePlateNumber(request.getLicensePlateNumber())
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
        car.setLicensePlateNumber(request.getLicensePlateNumber());

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

    public CarDTO.Response.SimpleRequest deleteCar(Long id) {
        getCarOrThrow(id);
        carRepository.deleteById(id);
        return new CarDTO.Response.SimpleRequest("success");
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
                car.getLicensePlateNumber(),
                car.getStatus(),
                car.getUserEmail(),
                car.getCreatedAt(),
                car.getUpdatedAt()
        );
    }


    public CarDTO.Response.FullInfo editStatus(Long id, CarDTO.Request.UpdateStatus status) {
        Car car = getCarOrThrow(id);
        car.setStatus(status.getStatus());
        log.info("Updating car status to " + status.getStatus());
        car = carRepository.save(car);
        return toDto(car);
    }
}
