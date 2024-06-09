package com.adjoda.step;

import com.adjoda.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@Order(2)
@Slf4j
public class ServiceTwoStep implements RequestStep {
    private RequestStep nextStep;

    @Override
    public void execute(RequestDto requestDto) {
        var message = MessageFormat.format(
                "Calling ServiceTwoStep with arguments: {0} {1} {2}",
                requestDto.id(), requestDto.name(), Thread.currentThread().getName()
        );
        System.out.println(message);

        if(nextStep != null) {
            nextStep.execute(requestDto);
        }
    }

    @Override
    public void setNextStep(RequestStep step) {
        this.nextStep = step;
    }
}
