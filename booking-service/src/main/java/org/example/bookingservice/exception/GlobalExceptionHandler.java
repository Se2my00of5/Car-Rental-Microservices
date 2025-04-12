package org.example.bookingservice.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.SimpleErrorResponseDTO;
import exception.BadRequestException;
import exception.NotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SimpleErrorResponseDTO> handleNotFound(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new SimpleErrorResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<SimpleErrorResponseDTO> handleBadRequest(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SimpleErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<SimpleErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Ошибка целостности данных";

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException constraintEx) {
            String constraintName = constraintEx.getConstraintName();
            if ("uc_cars_licenseplatenumber".equals(constraintName)) {
                message = "Автомобиль с таким номером уже существует";
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SimpleErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<SimpleErrorResponseDTO> handleFeignException(FeignException ex) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Optional<ByteBuffer> responseBody = ex.responseBody();
            if (responseBody.isPresent()) {
                ByteBuffer buffer = responseBody.get();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                JsonNode root = mapper.readTree(bytes);

                int status = root.has("status") ? root.get("status").asInt() : 500;
                String message = root.has("message") ? root.get("message").asText() : "Unknown error";

                // Создаём вручную
                SimpleErrorResponseDTO error = new SimpleErrorResponseDTO(status, message);

                return ResponseEntity.status(status).body(error);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SimpleErrorResponseDTO(500, "Internal server error: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleErrorResponseDTO> handleOtherExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SimpleErrorResponseDTO(500, "Internal server error: " + ex.getMessage()));
    }
}