package org.example.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookingservice.service.BookingService;
import org.example.commonservice.dto.BookingDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/bookings")
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Создать бронирование", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookingDTO.Response.Info> createBooking(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody BookingDTO.Request.Create request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(authHeader, request));
    }

    @PostMapping("/complete")
    @Operation(summary = "Завершить аренду", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookingDTO.Response.Info> completeRented(
            @Valid @RequestBody BookingDTO.Request.Complete request
    ) {
        return ResponseEntity.ok(bookingService.completeRented(request));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "История аренды пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<BookingDTO.Response.Info>> getUserBookings(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "false") boolean isAdmin
    ) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId, isAdmin));
    }

}