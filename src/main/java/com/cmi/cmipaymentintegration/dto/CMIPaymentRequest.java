package com.cmi.cmipaymentintegration.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

// this meant to be a server-to-server message that CMI send after finishing a process of payment
// it's sometimes called instant payment notification or webhook

@Data
@Builder
public class CMIPaymentRequest {
    private String clientId;
    private BigDecimal amount;
    private String currency;
    private String oid; //Your own unique transaction ref
    private String okUrl; // URL CMI redirects to after successful payment
    private String failUrl; // URL CMI redirects to if the payment fails or canceled
    private String callbackUrl; // s2s notification endpoint for final confirmation
    private String tranType; // Transaction type, often "Auth" for authorization
    private String instalment; // Number of installments if you offer the option
    private String email; // Customer email
    private String tel; // Customer phone number
    private String BillToName; // Name of the cardholder or billing contact
    private String encoding; // Character set used
    private String hash; // Security hash you compute "SHA256" , "HMAC" or per CMI specification
    private String hashAlgorithm; // algorithm name used for the hash
    private String lang; // language code "en", "fr", "ar"
    private String rnd; // None used in the hash to prevent the replay attacks
    private String storetype; // Gateway mode
}
