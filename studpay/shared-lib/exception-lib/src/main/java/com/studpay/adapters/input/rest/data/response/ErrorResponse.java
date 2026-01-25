package com.studpay.adapters.input.rest.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String apiPath;
    private HttpStatus status;
    private String errorKey;
    private String errorMessage;
    private LocalDateTime errorTime;
    private List<String> methodArgErrors;
}
