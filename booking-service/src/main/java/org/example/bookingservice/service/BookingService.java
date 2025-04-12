package org.example.bookingservice.service;

import dto.SimpleErrorResponseDTO;
import exception.BadRequestException;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.example.bookingservice.client.CarServiceClient;
import org.example.bookingservice.client.UserServiceClient;
import org.example.bookingservice.mapper.BookingMapper;
import org.example.bookingservice.model.Booking;
import org.example.bookingservice.repository.BookingRepository;
import org.example.commonservice.dto.BookingDTO;
import org.example.commonservice.dto.CarDTO;
import org.example.commonservice.dto.CarStatus;
import org.example.commonservice.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final CarServiceClient carServiceClient;
    private final UserServiceClient userServiceClient;
    private final BookingRepository bookingRepository;
    //private final JwtProvider jwtProvider;
    private final BookingMapper bookingMapper;

    // Создание бронирования
    public BookingDTO.Response.Info createBooking(String authHeader, BookingDTO.Request.Create request) {
        CarStatus carStatus = carServiceClient
                .getCarById(request.getCarId())
                .getBody()
                .getStatus();

        if (carStatus != CarStatus.AVAILABLE) {
            throw new BadRequestException("Машина недоступна для бронирования");
        }

        // Переводим в статус "Забронировано"
        carServiceClient.editStatus(request.getCarId(), new CarDTO.Request.UpdateStatus(CarStatus.BOOKED));

        Booking booking = Booking.builder()
                .carId(request.getCarId())
                .userId(userServiceClient.getMyProfile(authHeader).getBody().getId())
                .endAt(request.getEndAt())
                .paymentConfirmed(false)
                .build();

        booking = bookingRepository.save(booking);

        log.info("booking: {}", booking);

        return bookingMapper.toResponseInfoDTO(booking);
    }

    // Завершение аренды
    public BookingDTO.Response.Info completeRented(BookingDTO.Request.Complete request) {
        Booking booking = bookingRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.isPaymentConfirmed()) {
            throw new BadRequestException("Аренда не активна");
        }

        booking.setEndedAt(LocalDateTime.now());

        carServiceClient.editStatus(booking.getCarId(), new CarDTO.Request.UpdateStatus(CarStatus.AVAILABLE));

        bookingRepository.save(booking);

        return bookingMapper.toResponseInfoDTO(booking);
    }

    // История бронирований пользователя с id
    public List<BookingDTO.Response.Info> getUserBookingsById(Long userId) {
        userServiceClient.getProfileById(userId);

        return bookingRepository.findByUserId(userId).stream()
                .map(bookingMapper::toResponseInfoDTO)
                .collect(Collectors.toList());
    }

    // История бронирований пользователя
    public List<BookingDTO.Response.Info> getUserBookings(String authHeader) {
        Long userId = userServiceClient
                .getMyProfile(authHeader)
                .getBody()
                .getId();

//        List<Booking> bookings = bookingRepository.findByUserId(userId);
//        log.info("bookings: {}", bookings);
//        log.info("bookings[0].getId(): {}", bookings.getFirst().getId());
//        List<BookingDTO.Response.Info> bookings1 = bookingRepository.findByUserId(userId).stream()
//                .map(mapper -> bookingMapper.toResponseInfoDTO(mapper))
//                .collect(Collectors.toList());

        //log.info("bookings1[0].getId(): {}", bookings1.getFirst().getId());
        //log.info("bookings1: {}", bookings1);
        return bookingRepository.findByUserId(userId).stream()
                .map(bookingMapper::toResponseInfoDTO)
                .collect(Collectors.toList());
    }

    // История бронирований автомобиля (только для админа)
    public List<BookingDTO.Response.Info> getCarBookings(Long carId) {
        carServiceClient.getCarById(carId);

        return bookingRepository.findByCarId(carId).stream()
                .map(bookingMapper::toResponseInfoDTO)
                .collect(Collectors.toList());
    }
    
}