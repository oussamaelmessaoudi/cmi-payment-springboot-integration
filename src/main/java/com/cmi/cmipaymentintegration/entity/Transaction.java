package com.cmi.cmipaymentintegration.entity;

import com.cmi.cmipaymentintegration.validation.ValidPaymentMethod;
import com.cmi.cmipaymentintegration.validation.ValidTransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // auto-incremented/ generated id (db primary key)

    @Column(name="transaction_id",nullable = false, unique = true)
    private String transactionId; // transaction order reference before sending to CMI gateway

    @Column(name="cmi_transaction_id",unique = true)
    private String cmiTransactionId; // the unique id returns by cmi after transaction is processed
    //it may be null until the process of payment finishes

    @Column(name="merchant_id",nullable = false)
    private String merchantId; // your CMI merchant ID

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name="transaction_status")
    @ValidTransactionStatus
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_method")
    @ValidPaymentMethod
    private PaymentMethod paymentMethod;


    @Column(columnDefinition = "TEXT")
    private String description;

    private String returnUrl; // Url used by cmi to redirect user
    private String callbackUrl; // Url to send s2s notifications

    @Column(columnDefinition = "TEXT")
    private String cmiResponse; // the payload/ raw JSON received from the cmi ("TEXT" in order to handle long payloads)

    @Column(columnDefinition = "TEXT")
    private String failureReason; // extracting and storing the failure reason from payload if provided

    @Column(nullable = false)
    private String currency; // currency code (USD,MAD, EUR ...)

    //Customer Info
    private String customerEmail;
    private String customerName;
    private String customerPhone;


    @CreationTimestamp
    private LocalDateTime createdAt; //Filled automatically once the row is inserted

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Updated automatically once the row is changed

    private LocalDateTime processedAt; // After finishing payment, it will be handled by the service layer

    @Version
    private Long version; //Implementing optimistic locking (prevents concurrency issues)

    //Audit fields
    private String clientIp; //Capturing the requester's ip
    private String userAgent; // and agent string for audit and fraud checks

    @ElementCollection
    @CollectionTable(name="transaction_metadata")
    @MapKeyColumn(name="key_name")
    @Column(name="value")
    private Map<String,String> metadata = new HashMap<>(); // storing extra infos in another table named transaction_metadata
}
