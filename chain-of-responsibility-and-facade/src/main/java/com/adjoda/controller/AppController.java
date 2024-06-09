package com.adjoda.controller;


import com.adjoda.dto.RequestDto;
import com.adjoda.facade.RequestFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AppController {

    private final RequestFacade requestFacade;

    @PostMapping("/process")
    public void processRequest(@RequestBody RequestDto requestDto) {
        requestFacade.processRequest(requestDto);
    }
}
