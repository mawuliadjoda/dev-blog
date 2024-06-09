package com.adjoda.service;

import com.adjoda.dto.RequestDto;
import com.adjoda.handler.ServiceFourHandler;
import com.adjoda.handler.ServiceOneHandler;
import com.adjoda.handler.ServiceThreeHandler;
import com.adjoda.handler.ServiceTwoHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AppService {

    private ServiceOneHandler serviceOneHandler;
    private ServiceTwoHandler serviceTwoHandler;
    private ServiceThreeHandler serviceThreeHandler;
    private ServiceFourHandler serviceFourHandler;


    public String processRequest(RequestDto requestDto) {
        log.info("Request process initiated {}", requestDto.name());

        serviceOneHandler.execute(requestDto);
        serviceTwoHandler.execute(requestDto);
        serviceThreeHandler.execute(requestDto);
        serviceFourHandler.execute(requestDto);

        return "Request " + requestDto.name() + " with ID " + requestDto.id() + " executed successfully!" + "In Thead:" + Thread.currentThread().getName();
    }
}
