package com.cmi.cmipaymentintegration.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PaymentMethodValidator implements ConstraintValidator<ValidPaymentMethod, String> {

    private static final Set<String> METHODS = Set.of("CREDIT_CARD", "DEBIT_CARD", "MOBILE_WALLET", "BANK_TRANSFER");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if(value==null ||value.isEmpty()) return false;
        return METHODS.contains(value);
    }

}
