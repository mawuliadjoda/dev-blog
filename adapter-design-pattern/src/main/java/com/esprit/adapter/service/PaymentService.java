package com.esprit.adapter.service;

import com.esprit.adapter.controller.data.input.PaymentRequest;
import com.esprit.adapter.controller.data.output.PaymentResponse;
import com.esprit.adapter.processor.PaymentProcessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private Map<String, PaymentProcessor> paymentProcessorMap;

    public PaymentService(List<PaymentProcessor> paymentProcessors) {
        paymentProcessorMap = paymentProcessors.stream()
                .collect(Collectors.toMap(paymentProcessor -> paymentProcessor.getClass().getSimpleName(), Function.identity()));
    }

    public PaymentResponse processPayment(String gateway, PaymentRequest paymentRequest) {
        PaymentProcessor paymentProcessor = paymentProcessorMap.get(gateway + "Adapter");
        paymentProcessor.makePayement(paymentRequest.amount());
        return new PaymentResponse(true, new Random().nextLong(100000000000L));
    }

    /*
    public PaymentResponse processPayment(String gateway, PaymentRequest paymentRequest) {

        switch (gateway.toLowerCase()) {
            case "Paypal":
                paypalService.makePayement(paymentRequest.amount());
                break;

            case "GPay":
                gPaySrevie.makePayement(paymentRequest.amount());
                break;
        }

        return new PaymentResponse(true, new Random().nextLong(100000000000L));
    }

     */


}
