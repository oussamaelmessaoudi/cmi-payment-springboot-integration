package com.cmi.cmipaymentintegration.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "cmi")
@Data
@Component
@Validated
public class CMIProperties {
    @NotBlank(message = "CMI base url is required")
    @URL(message = "Invalid base URL format")
    private String baseUrl;

    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "CMI store key is required")
    private String storeKey;

    private String hashAlgorithm = "SHA512";

    @Min(value = 5, message = "Timeout must be over 5 seconds")
    private int timeout = 30;

    @Min(value = 1, message = "Max number of retries must be at least 1")
    @Max(value = 5, message = "Max number of retries must not exceed 5")
    private int maxRetries = 3;

    @NotBlank(message = "Callback url is required")
    @URL(message = "Invalid callback URL format")
    private String callbackUrl;

    @NotBlank(message = "Return URL is required")
    @URL(message = "Invalid return URL format")
    private String returnUrl;

}
