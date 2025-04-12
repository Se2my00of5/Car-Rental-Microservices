package org.example.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleErrorResponseDTO {
    @JsonProperty("status")
    private int status;

    @JsonProperty("message")
    private String message;
}