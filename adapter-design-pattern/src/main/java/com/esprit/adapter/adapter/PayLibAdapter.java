package com.esprit.adapter.adapter;

import com.esprit.adapter.processor.PaymentProcessor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PayLibAdapter implements PaymentProcessor {
    @Override
    public void makePayement(double amount) {
        log.info("Payment processed via PayLib with amount: {}", amount);
    }
}
