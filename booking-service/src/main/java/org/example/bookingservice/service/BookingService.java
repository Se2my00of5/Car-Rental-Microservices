package org.example.bookingservice.service;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public BookingDTO.Response.Info completeRented(Long id) {
        Booking booking = bookingRepository.findById(id)
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

    public BookingDTO.Response.Message checkCar(Long id) {
        CarStatus status = carServiceClient.getCarById(id).getBody().getStatus();

        String message = CarStatus.AVAILABLE == status ? "AVAILABLE" : "UNAVAILABLE";

        return new BookingDTO.Response.Message(message);
    }


    // будет вызываться когда оплата не произошла за определённое время(отмена бронирования)
    public BookingDTO.Response.Message deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + id + " не найдено"));

        bookingRepository.delete(booking);

        carServiceClient.editStatus(booking.getCarId(), new CarDTO.Request.UpdateStatus(CarStatus.AVAILABLE));

        return new BookingDTO.Response.Message("Бронирование успешно удалено");
    }

    @Scheduled(fixedRate = 60000)
    @Transactional // завершает аренду, если время пришло и EndedAt = null
    public void autoCompleteExpiredBookings() {
        List<Booking> expiredBookings = bookingRepository.findByEndAtBeforeAndEndedAtIsNull(LocalDateTime.now());

        for (Booking booking : expiredBookings) {
            booking.setEndedAt(LocalDateTime.now());

            carServiceClient.editStatus(booking.getCarId(), new CarDTO.Request.UpdateStatus(CarStatus.AVAILABLE));

            bookingRepository.save(booking);
        }
    }

    public BookingDTO.Response.Message startRentedBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + id + " не найдено"));

        booking.setPaymentConfirmed(true);

        carServiceClient.editStatus(booking.getCarId(), new CarDTO.Request.UpdateStatus(CarStatus.RENTED));

        return new BookingDTO.Response.Message("Аренда начата");
    }
}