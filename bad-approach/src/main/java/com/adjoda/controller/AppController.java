package com.adjoda.controller;

import com.adjoda.dto.RequestDto;
import com.adjoda.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AppController {

    private final AppService appService;

    @PostMapping("/process")
    public String processRequest(@RequestBody RequestDto requestDto) {
        return appService.processRequest(requestDto);
    }
}
