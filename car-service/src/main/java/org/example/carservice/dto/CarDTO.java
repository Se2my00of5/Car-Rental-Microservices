package org.example.carservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Value;
import org.example.carservice.model.CarStatus;

import java.time.LocalDateTime;

public enum CarDTO {
    ;

    public enum Request {
        ;

        @Value
        public static class Create implements Brand, Model, Color, Year, Status, UserEmail {

            @NotBlank
            @Schema(description = "Бренд автомобиля", example = "Toyota")
            String brand;

            @NotBlank
            @Schema(description = "Модель автомобиля", example = "Camry")
            String model;

            @NotBlank
            @Schema(description = "Цвет автомобиля", example = "Black")
            String color;

            @Min(1900)
            @Max(2100)
            @Schema(description = "Год выпуска", example = "2020")
            int year;

            @NotNull
            @Schema(description = "Статус автомобиля", example = "AVAILABLE")
            CarStatus status;

            @NotBlank
            @Email
            @Schema(description = "Email пользователя, создавшего запись", example = "user@example.com")
            String userEmail;
        }

        @Value
        public static class Update implements Brand, Model, Color, Year, Status {

            @NotBlank
            @Schema(description = "Бренд автомобиля", example = "Toyota")
            String brand;

            @NotBlank
            @Schema(description = "Модель автомобиля", example = "Camry")
            String model;

            @NotBlank
            @Schema(description = "Цвет автомобиля", example = "Black")
            String color;

            @Min(1900)
            @Max(2100)
            @Schema(description = "Год выпуска", example = "2020")
            int year;

            @NotNull
            @Schema(description = "Статус автомобиля", example = "AVAILABLE")
            CarStatus status;
        }
    }

    public enum Response {
        ;

        @Value
        public static class FullInfo implements Id, Brand, Model, Color, Year, Status, UserEmail, CreatedAt, UpdatedAt {

            @Positive
            @Schema(description = "ID автомобиля", example = "1")
            Long id;

            @NotBlank
            @Schema(description = "Бренд автомобиля", example = "Toyota")
            String brand;

            @NotBlank
            @Schema(description = "Модель автомобиля", example = "Camry")
            String model;

            @NotBlank
            @Schema(description = "Цвет автомобиля", example = "Black")
            String color;

            @Min(1900)
            @Max(2100)
            @Schema(description = "Год выпуска", example = "2020")
            int year;

            @NotNull
            @Schema(description = "Статус автомобиля", example = "AVAILABLE")
            CarStatus status;

            @NotBlank
            @Email
            @Schema(description = "Email пользователя, добавившего автомобиль", example = "user@example.com", format = "email")
            String userEmail;

            @PastOrPresent
            @Schema(description = "Дата создания записи", example = "2025-04-06T12:00:00")
            LocalDateTime createdAt;

            @PastOrPresent
            @Schema(description = "Дата последнего изменения записи", example = "2025-04-06T12:30:00")
            LocalDateTime updatedAt;
        }
    }

    // ===== Интерфейсы для общих полей =====

    public interface Id {
        @Positive
        Long getId();
    }

    public interface Brand {
        @NotBlank
        String getBrand();
    }

    public interface Model {
        @NotBlank
        String getModel();
    }

    public interface Color {
        @NotBlank
        String getColor();
    }

    public interface Year {
        @Min(1900)
        @Max(2100)
        int getYear();
    }

    public interface Status {
        @NotNull
        CarStatus getStatus();
    }

    public interface UserEmail {
        @NotBlank
        @Email
        String getUserEmail();
    }

    public interface CreatedAt {
        @PastOrPresent
        LocalDateTime getCreatedAt();
    }

    public interface UpdatedAt {
        @PastOrPresent
        LocalDateTime getUpdatedAt();
    }
}