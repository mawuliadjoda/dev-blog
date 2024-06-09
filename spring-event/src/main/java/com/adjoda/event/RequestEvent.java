package com.adjoda.event;

import com.adjoda.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class RequestEvent extends ApplicationEvent {
    private RequestDto requestDto;

    public RequestEvent(Object source, RequestDto requestDto) {
        super(source);
        this.requestDto = requestDto;
    }
}
