package org.example.commonservice.dto;

public enum CarStatus {
    AVAILABLE,      // доступен для бронирования
    BOOKED,         // забронирован
    RENTED,         // арендован
    IN_REPAIR       // на ремонте
}