package org.example.carservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.carservice.dto.CarDTO;
import org.example.carservice.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/car")
@RequiredArgsConstructor
public class UserCarController {

    private final CarService carService;

    @GetMapping
    @Operation(summary = "Получение списка всех автомобилей", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CarDTO.Response.FullInfo>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/available")
    @Operation(summary = "Получение списка доступных для аренды автомобилей", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CarDTO.Response.FullInfo>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об автомобиле по ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.FullInfo> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

}