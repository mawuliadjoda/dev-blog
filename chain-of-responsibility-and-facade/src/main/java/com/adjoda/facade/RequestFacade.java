package com.adjoda.facade;

import com.adjoda.dto.RequestDto;
import com.adjoda.step.RequestStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestFacade {
    private RequestStep firstStep;

    public RequestFacade(List<RequestStep> steps) {
        firstStep = steps.getFirst();
        for (int i = 0; i < steps.size(); i++) {
            var current = steps.get(i);
            var next = i < steps.size() - 1 ? steps.get(i + 1) : null;
            current.setNextStep(next);
        }
    }

    public void processRequest(RequestDto requestDto) {
        firstStep.execute(requestDto);
    }
}
