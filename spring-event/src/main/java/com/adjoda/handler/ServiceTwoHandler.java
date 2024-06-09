package com.adjoda.handler;

import com.adjoda.event.RequestEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class ServiceTwoHandler {

    @EventListener
    @Async
    public void execute(RequestEvent event) {
        // ServiceTwoHandler details
        var message = MessageFormat.format(
                "Calling ServiceTwoHandler with arguments: {0} {1} {2}",
                event.getRequestDto().id(), event.getRequestDto().name(), Thread.currentThread().getName()
        );
        System.out.println(message);
    }
}
