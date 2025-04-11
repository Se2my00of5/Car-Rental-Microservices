package org.example.carservice.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.carservice.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.commonservice.dto.CarDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/car")
@RequiredArgsConstructor
public class AdminCarController {

    private final CarService carService;

    @PostMapping
    @Operation(summary = "Добавление нового автомобиля", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.FullInfo> createCar(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid CarDTO.Request.Create request
    ) {
        return ResponseEntity.ok(carService.createCar(authHeader, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление данных об автомобиле", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.FullInfo> updateCar(
            @PathVariable Long id,
            @RequestBody @Valid CarDTO.Request.Update request
    ) {
        return ResponseEntity.ok(carService.updateCar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление автомобиля", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.SimpleRequest> deleteCar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(carService.deleteCar(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Изменить статус автомобиля", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CarDTO.Response.FullInfo> editStatus(@PathVariable Long id, @RequestBody CarDTO.Request.UpdateStatus status) {
        return ResponseEntity.ok(carService.editStatus(id, status));
    }
}