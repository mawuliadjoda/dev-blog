package com.studpay.template.adapters.inbound.rest;


import com.studpay.template.domain.model.Greeting;
import lombok.RequiredArgsConstructor;
//import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class MessageController {

    //private final StreamBridge streamBridge;

    @PostMapping("/send")
    public String sendMessage(@RequestBody Greeting greeting) {
        //streamBridge.send("produceMessage-out-0", greeting);
        return "Message sent: " + greeting;
    }
}