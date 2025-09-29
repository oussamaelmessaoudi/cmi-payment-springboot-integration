package com.cmi.cmipaymentintegration.dto;

import com.cmi.cmipaymentintegration.entity.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInitiationResponse {
    private String transactionId;
    private String paymentUrl;
    private TransactionStatus transactionStatus;
    private LocalDateTime createdAt;
    private String message;
}
