package org.example.carservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleErrorResponseDTO {
    private int status;
    private String message;
}