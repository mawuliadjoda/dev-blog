package com.adjoda.handler;

import com.adjoda.dto.RequestDto;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class ServiceTwoHandler {

    public void execute(RequestDto dto) {
        // ServiceTwoHandler details
        var message = MessageFormat.format(
                "Calling ServiceTwoHandler with arguments: {0} {1} {2}",
                dto.id(), dto.name(), Thread.currentThread().getName()
        );
        System.out.println(message);
    }
}
