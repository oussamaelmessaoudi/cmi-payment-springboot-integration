package com.cmi.cmipaymentintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Valid
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInitiationRequest {
    @NotBlank(message = "Transaction ID is required")
    @Size(max = 50, message = "Identifier must not exceed 50 characters ")
    private String transactionId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.1",message = "Amount must be greater than 0.0")
    @Digits(integer = 10,fraction = 2,message = "Invalid amount format")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$",message = "Currency must be 3-uppercase-letters ISO code")
    private String currency;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String customerPhone;

    @Size(max=255, message = "Description must not exceed 255 characters")
    private String description;

    @Valid
    private Map<String,String> metadata = new HashMap<>();

    @NotBlank(message = "Return URL is required")
    @URL(message = "Invalid return URL format")
    private String returnUrl;
}
