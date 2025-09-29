package com.cmi.cmipaymentintegration.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

// this meant to be a server-to-server message that CMI send after finishing a process of payment
// it's sometimes called instant payment notification or webhook

@Data
public class CMICallbackRequest {
    private String clientId;
    private String orderId; // your own order/transaction id
    private String AuthCode; // authorization code from the card network
    private String ProcReturnCode; // a code shows or indicates the processing result
    private String Response; //Categorical text such as "Approved" or "Declined"
    private String mdStatus; // Secure status, usually "1" means success and other numbers are issues related
    private String mdErrorMsg; // error text in case secure failed
    private String ErrMsg; // General error desc
    private String EXTRA_TRXDATE; //Transaction date/time as recorded by CMI
    private String HASH; // Crypted hash to prove that the msg came from CMI
    private String HASHPARAMS; // list of fields used to compute the hash
    private String HASHPARAMSVAL; //concatenated values of those fields before hashing
    private String currency;
    private BigDecimal amount;
    private String rnd; // Random string CMI generated for hash protection

}
