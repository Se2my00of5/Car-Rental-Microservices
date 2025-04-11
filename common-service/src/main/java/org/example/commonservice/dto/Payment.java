package org.example.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    private Long id;

    private String externalPaymentId; // ID из внешнего сервиса

    private boolean success;

    private LocalDateTime createdAt;

}