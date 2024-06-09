package com.adjoda.service;

import com.adjoda.dto.RequestDto;
import com.adjoda.event.RequestEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AppService {

    private ApplicationEventPublisher applicationEventPublisher;

    public String processRequest(RequestDto requestDto) {
        log.info("Request process initiated {}", requestDto.name());

        applicationEventPublisher.publishEvent(new RequestEvent(this, requestDto));

        return "Request " + requestDto.name() + " with ID " + requestDto.id() + " executed successfully!" + "In Thead:" + Thread.currentThread().getName();
    }
}
