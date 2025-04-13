package org.example.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.example.bookingservice.service.BookingService;
import org.example.commonservice.dto.BookingDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingService bookingService;

    @DeleteMapping("{id}")
    @Operation(summary = "Удаление записи бронирования", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookingDTO.Response.Message> deleteBooking(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(bookingService.deleteBooking(id));
    }

    @PostMapping("{id}/pay")
    @Operation(summary = "Подтвердить оплату аренды", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookingDTO.Response.Message> startRentedBookingById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(bookingService.startRentedBookingById(id));
    }



    @GetMapping("/car/{carId}")
    @Operation(summary = "История аренды автомобиля", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<BookingDTO.Response.Info>> getCarBookings(
            @PathVariable Long carId
    ) {
        return ResponseEntity.ok(bookingService.getCarBookings(carId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "История аренды пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<BookingDTO.Response.Info>> getUserBookings(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(bookingService.getUserBookingsById(userId));
    }


}