package org.example.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.commonservice.kafka.MultiProducer;
import org.example.bookingservice.service.BookingService;
import org.example.commonservice.dto.BookingDTO;
import org.example.commonservice.dto.KafkaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/bookings")
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingService bookingService;
    private final MultiProducer multiProducer;

    @PostMapping
    @Operation(summary = "Создать бронирование", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookingDTO.Response.Info> createBooking(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody BookingDTO.Request.Create request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(authHeader, request));
    }

    @GetMapping("/check/car/{id}")
    @Operation(summary = "Проверить машину на доступность", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookingDTO.Response.Message> checkCar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(bookingService.checkCar(id));
    }

    @PostMapping("{id}/complete")
    @Operation(summary = "Завершить аренду", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookingDTO.Response.Info> completeRented(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(bookingService.completeRented(id));
    }

    @GetMapping("/user")
    @Operation(summary = "История аренды пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<BookingDTO.Response.Info>> getUserBookings(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(bookingService.getUserBookings(authHeader));
    }



    @PostMapping("/kafka")
    @Operation(summary = "отправить смс", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> postKafka(
            @RequestBody KafkaDTO.MessageInTopic messageInTopic
            ) {
        multiProducer.sendString(messageInTopic.getTopic(), messageInTopic.getMessage());
        return ResponseEntity.ok("");
    }

}