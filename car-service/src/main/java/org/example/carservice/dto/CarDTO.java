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
        public static class Create implements Brand, Model, Color, Year, LicensePlateNumber {

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

            @Pattern(
                    regexp = "^[A-Z]{1}\\d{3}[A-Z]{2}\\d{2,3}$",
                    message = "Номер автомобиля должен быть в формате A123BC77"
            )
            @Schema(description = "Автомобильный номер", example = "A123BC77")
            String licensePlateNumber;

        }

        @Value
        public static class Update implements Brand, Model, Color, Year, LicensePlateNumber {

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

            @Pattern(
                    regexp = "^[A-Z]{1}\\d{3}[A-Z]{2}\\d{2,3}$",
                    message = "Номер автомобиля должен быть в формате A123BC77"
            )
            @Schema(description = "Автомобильный номер", example = "A123BC77")
            String licensePlateNumber;

        }
    }

    public enum Response {
        ;

        @Value
        public static class FullInfo implements Id, Brand, Model, Color, Year, LicensePlateNumber, Status, UserEmail, CreatedAt, UpdatedAt {

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

            @Pattern(
                    regexp = "^[A-Z]{1}\\d{3}[A-Z]{2}\\d{2,3}$",
                    message = "Номер автомобиля должен быть в формате A123BC77"
            )
            @Schema(description = "Автомобильный номер", example = "A123BC77")
            String licensePlateNumber;

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

        @Value
        public static class SimpleRequest implements Message{
            String message;
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

    public interface LicensePlateNumber {
        @Pattern(
                regexp = "^[A-Z]{1}\\d{3}[A-Z]{2}\\d{2,3}$",
                message = "Номер автомобиля должен быть в формате A123BC77"
        )
        String getLicensePlateNumber();
    }

    public interface Message {
        @NotBlank
        String getMessage();
    }
}