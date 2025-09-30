package com.cmi.cmipaymentintegration.service;

import com.cmi.cmipaymentintegration.config.CMIProperties;
import com.cmi.cmipaymentintegration.dto.CMICallbackRequest;
import com.cmi.cmipaymentintegration.dto.CMIPaymentRequest;
import com.cmi.cmipaymentintegration.exception.CMIClientException;
import com.cmi.cmipaymentintegration.exception.CMIServerException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class CMIClientService {
    private final WebClient webClient;
    private final CMIProperties cmiProperties;
    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void init(){
        Timer.Sample sample = Timer.start(meterRegistry);

        sample.stop(Timer.builder("cmi.client.init")
                        .register(meterRegistry));
    }

    //
    public Mono<String> initiatePayment(CMIPaymentRequest request){
        return  Mono.defer(()->{

            Timer.Sample sample = Timer.start(meterRegistry);

            return webClient.post()
                    .uri(cmiProperties.getBaseUrl()+"/fim/est3Dgate")
                    //make a post request to 3d secure endpoint
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData(buildFormData(request)))
                    // sending the data as application form url encoded using buildFormData
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> {
                        meterRegistry.counter("cmi.client.error","type","4xx").increment();
                        return Mono.error(new CMIClientException("Client error: "+response.statusCode()))
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, response ->{
                        meterRegistry.counter("cmo.client.error","type","5xx").increment();
                        return Mono.error(new CMIServerException("Server error: "+response.statusCode()));
                    })
                    //Error Handling, handles errors client-related(4xx) and server-related(5xx) separately
                    .bodyToMono(String.class)
                    //Converts the response body into a Mono<String>
                    .timeout(Duration.ofSeconds(cmiProperties.getTimeout()))
                    //if the CMI didn't response in time, the Mono fails due to TimeoutException
                    .retryWhen(Retry.backoff(cmiProperties.getMaxRetries(),Duration.ofSeconds(1))
                            .filter(throwable -> throwable instanceof CMIServerException)
                    )
                    // Retries on server issues only, using exponential backof which depends on the max retries value defined in the cmi configuration
                    .doOnSuccess(response ->{
                        meterRegistry.counter("cmi.client.success").increment();
                        log.debug("CMI payment initiation successful for OID : {}",request.getOid());
                    })
                    // in case we have a successful payment -> increments success counter and debug the success with order id
                    .doOnError(error ->{
                        meterRegistry.counter("cmi.client.failure").increment();
                        log.debug("CMI payment initiation failed for OID : {}",request.getOid());
                    })
                    // on the contrary -> increments the failure counter and debug the failure with order id
                    .doFinally(signalType -> {
                                sample.stop(Timer.builder("cmi.client.payment.initiation").register(meterRegistry));
                            });
        });
    }

    //So here we used MultiValueMap instead of a simple Map cuz we want to store multiple values for a common key for example
    // formData.add("oid1","123")
    // formData.add("oid1","456")
    private MultiValueMap<String,String> buildFormData(CMIPaymentRequest request){
        MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();

        formData.add("clientId",request.getClientId());
        formData.add("amount",request.getAmount().toString());
        formData.add("currency",request.getCurrency());
        formData.add("oid",request.getOid());
        formData.add("okUrl",request.getOkUrl());
        formData.add("failUrl",request.getFailUrl());
        formData.add("callbackUrl",request.getCallbackUrl());
        formData.add("tranType",request.getTranType());
        formData.add("instalment",request.getInstalment());
        formData.add("email",request.getEmail());
        formData.add("tel",request.getTel());
        formData.add("BillToName",request.getBillToName());
        formData.add("encoding",request.getEncoding());
        formData.add("rnd",request.getRnd());
        formData.add("storetype",request.getStoretype());
        formData.add("hash",request.getHash());
        formData.add("hashAlgorithm",request.getHashAlgorithm());
        formData.add("lang",request.getLang());

        return formData;
    }

    public boolean validateCallback(CMICallbackRequest request){
        try{
            String computeHash = computeHash(request);
            boolean isValid = computeHash.equals(request.getHASH());

            meterRegistry.counter("cmi.callback.validation","result", isValid ? "valid": "invalid").increment();

            if(!isValid)
                log.warn("Invalid callback hash for oid: {}",request.getOrderId());

            return isValid;
        }catch (Exception e){
            meterRegistry.counter("cmi.callback.valiation.error").increment();
            log.error("Error validation callback hash for oid : {}",request.getOrderId(),e);
            return false;
        }

    }

    private String computeHash(CMIPaymentRequest request){
        //CMI Hash calculation : clientid|oid|amount|okUrl|failUrl|TranType|instalment|rnd|storekey

        String data = String.join("|",
                request.getClientId(),
                request.getOid(),
                request.getAmount().toString(),
                request.getOkUrl(),
                request.getFailUrl(),
                request.getTranType(),
                request.getInstalment(),
                request.getRnd(),
                cmiProperties.getStoreKey());

        return DigestUtils.sha512Hex(data);
    }

    private String computeHash(CMICallbackRequest request){
        //CMI Callback Hash calculation
        String data = String.join("|",
                request.getClientId(),
                request.getOrderId(),
                request.getAuthCode(),
                request.getProcReturnCode(),
                request.getResponse(),
                request.getMdStatus(),
                request.getCurrency(),
                request.getAmount().toString(),
                request.getRnd(),
                cmiProperties.getStoreKey());

        return DigestUtils.sha512Hex(data);
    }
}
