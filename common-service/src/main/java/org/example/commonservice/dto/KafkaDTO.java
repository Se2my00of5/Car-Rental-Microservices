package org.example.commonservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

public enum KafkaDTO {;
    ;

    @Value
    public static class MessageInTopic implements Topic, Message {

        @NotBlank
        @Schema(description = "топик", example = "string")
        String topic;

        @NotBlank
        @Schema(description = "Сообщение", example = "string")
        String message;
    }


    @Value
    public static class OnlyMessage implements KafkaDTO.Message {

        @NotBlank
        @Schema(description = "Сообщение", example = "Аренда успешно завершена")
        String message;
    }


    @Value
    @Builder
    public static class ChangeRentStatus implements CarId, CarStatus, Message {

        @NotNull
        @Schema(description = "id машины", example = "1")
        Long carId;

        @NotBlank
        @Schema(description = "Статус машины", example = "AVAILABLE")
        org.example.commonservice.dto.CarStatus carStatus;

        @NotBlank
        @Schema(description = "Сообщение", example = "Аренда успешно завершена")
        String message;

    }


    // ======= Общие интерфейсы для полей DTO =======


    private interface Topic {
        String getTopic();
    }

    private interface Message {
        String getMessage();
    }

    private interface CarStatus {
        org.example.commonservice.dto.CarStatus getCarStatus();
    }

    private interface CarId {
        Long getCarId();
    }


    private interface ServiceName {
        String getServiceName();
    }

}
