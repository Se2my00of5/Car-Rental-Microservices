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
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @Operation(summary = "Добавление нового автомобиля", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.FullInfo> createCar(
            @RequestBody @Valid CarDTO.Request.Create request
    ) {
        return ResponseEntity.ok(carService.createCar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление данных об автомобиле", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.FullInfo> updateCar(
            @PathVariable Long id,
            @RequestBody @Valid CarDTO.Request.Update request
    ) {
        return ResponseEntity.ok(carService.updateCar(id, request));
    }

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

    @PatchMapping("/{id}/repair")
    @Operation(summary = "Перевести автомобиль в статус 'на ремонте'", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.FullInfo> markAsInRepair(@PathVariable Long id) {
        return ResponseEntity.ok(carService.markAsInRepair(id));
    }

    @PatchMapping("/{id}/available")
    @Operation(summary = "Перевести автомобиль в статус 'доступен'", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.FullInfo> markAsAvailable(@PathVariable Long id) {
        return ResponseEntity.ok(carService.markAsAvailable(id));
    }
}