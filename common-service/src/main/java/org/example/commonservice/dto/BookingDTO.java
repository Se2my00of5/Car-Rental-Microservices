package org.example.commonservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.time.LocalDateTime;

public enum BookingDTO {
    ;

    public enum Request {
        ;

        @Value
        public static class Create implements CarId, EndAt {

            @NotNull
            @Schema(description = "ID автомобиля", example = "101")
            Long carId;

            @NotNull
            @Future
            @Schema(description = "Дата окончания аренды", example = "2025-04-15T18:00:00")
            LocalDateTime endAt;
        }

        @Value
        public static class Complete implements Id {

            @NotNull
            @Positive
            @Schema(description = "ID бронирования", example = "1")
            Long id;
        }
    }

    public enum Response {
        ;

        @Value
        public static class Info implements Id, CarId, UserId, PaymentConfirmed, CreatedAt, EndAt, EndedAt {

            @NotNull
            @Schema(description = "ID бронирования", example = "1")
            Long id;

            @NotNull
            @Schema(description = "ID автомобиля", example = "101")
            Long carId;

            @NotNull
            @Schema(description = "ID пользователя", example = "202")
            Long userId;

            @NotNull
            @Schema(description = "Подтвержден ли платёж", example = "true")
            Boolean paymentConfirmed;

            @NotNull
            @Schema(description = "Дата создания бронирования", example = "2025-04-10T12:30:00")
            LocalDateTime createdAt;

            @NotNull
            @Schema(description = "Дата окончания аренды", example = "2025-04-15T18:00:00")
            LocalDateTime endAt;

            @Schema(description = "Дата завершения аренды", example = "2025-04-15T18:00:00")
            LocalDateTime endedAt;
        }

        @Value
        public static class Message implements BookingDTO.Message {

            @NotBlank
            @Schema(description = "Сообщение", example = "Аренда успешно завершена")
            String message;
        }
    }

    // ======= Общие интерфейсы для полей DTO =======

    private interface Id {
        @Positive
        Long getId();
    }

    private interface CarId {
        @Positive
        Long getCarId();
    }

    private interface UserId {
        @Positive
        Long getUserId();
    }

    private interface EndAt {
        @Future
        LocalDateTime getEndAt();
    }
    private interface EndedAt {
        @Future
        LocalDateTime getEndedAt();
    }

    private interface CreatedAt {
        LocalDateTime getCreatedAt();
    }

    private interface PaymentConfirmed {
        Boolean getPaymentConfirmed();
    }

    private interface Message {
        String getMessage();
    }
}