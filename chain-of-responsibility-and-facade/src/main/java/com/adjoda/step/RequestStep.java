package com.adjoda.step;

import com.adjoda.dto.RequestDto;

public interface RequestStep {
    void execute(RequestDto requestDto);
    void setNextStep(RequestStep requestStep);
}
