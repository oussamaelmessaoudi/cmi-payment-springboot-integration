package com.cmi.cmipaymentintegration.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class TransactionStatusValidator implements ConstraintValidator<ValidTransactionStatus,String> {
    private static final Set<String> STATUS =Set.of("PENDING",
            "PROCESSING",
            "SUCCESS",
            "FAILED",
            "CANCELLED",
            "REFUNDED",
            "PARTIALLY_REFUNDED",
            "EXPIRED");
    @Override
    public boolean isValid(String status, ConstraintValidatorContext context){
        if(status == null || status.isEmpty()) return false;
        return STATUS.contains(status);
    }
}
