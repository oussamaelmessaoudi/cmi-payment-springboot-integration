package com.cmi.cmipaymentintegration.dto;

import com.cmi.cmipaymentintegration.entity.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionStatusResponse {
    private String transactionId;
    private String cmiTransactionId;
    private TransactionStatus status;
    private BigDecimal amount;
    private String currency;
    private String customerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String failureReason;
    private Map<String,String> metadata;
}
