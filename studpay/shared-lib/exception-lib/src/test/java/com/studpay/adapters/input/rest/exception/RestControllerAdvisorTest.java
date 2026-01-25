package com.studpay.adapters.input.rest.exception;

import com.studpay.adapters.input.rest.data.response.ErrorResponse;
import com.studpay.adapters.input.rest.exception.utils.ExceptionUtility;
import com.studpay.domain.model.exception.AppBusinessException;
import com.studpay.domain.model.exception.AppNotFoundException;
import com.studpay.domain.model.exception.AppTechnicalException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RestControllerAdvisorTest {

    public static final String ERROR_TIME = "errorTime";
    public static final String TEST_API_PATH = "/test-api-path";

    @InjectMocks
    private RestControllerAdvisor restControllerAdvisor;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException  methodArgumentNotValidException;

    @Mock
    private HttpHeaders headers;

    @Mock
    private BindingResult bindingResult;


    @Test
    void handleAppTechnicalException() {
        // Given
        AppTechnicalException appTechnicalException = new AppTechnicalException("Technical Exception");

        BDDMockito.when(webRequest.getDescription(false)).thenReturn(TEST_API_PATH);

        //When
        ResponseEntity<Object> responseEntity = restControllerAdvisor.handleAppTechnicalException(appTechnicalException,webRequest);

        ErrorResponse expectedResponse = ErrorResponse.builder()
                .apiPath(TEST_API_PATH)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(appTechnicalException.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().ignoringFields(ERROR_TIME).isEqualTo(responseEntity.getBody());
    }

    @Test
    void handleAppBusinessException() {
        // Given
        AppBusinessException appBusinessException = new AppBusinessException("Business Exception", "errorKey");
        BDDMockito.when(webRequest.getDescription(false)).thenReturn(TEST_API_PATH);

        // When
        ResponseEntity<Object> responseEntity = restControllerAdvisor.handleAppBusinessException(appBusinessException,webRequest);

        // Then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse expectedResponse = ErrorResponse.builder()
                .apiPath(TEST_API_PATH)
                .status(HttpStatus.BAD_REQUEST)
                .errorKey(appBusinessException.getErrorKey())
                .errorMessage(appBusinessException.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().ignoringFields(ERROR_TIME).isEqualTo(responseEntity.getBody());

    }

    @Test
    void handleAppNotFoundException() {
        // Given
        AppNotFoundException appNotFoundException = new AppNotFoundException("Application not found");
        BDDMockito.when(webRequest.getDescription(false)).thenReturn(TEST_API_PATH);

        // When
        ResponseEntity<Object> responseEntity = restControllerAdvisor.handleAppNotFoundException(appNotFoundException,webRequest);

        // Then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ErrorResponse expectedResponse = ErrorResponse.builder()
                .apiPath(TEST_API_PATH)
                .status(HttpStatus.NOT_FOUND)
                .errorMessage(appNotFoundException.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().ignoringFields(ERROR_TIME).isEqualTo(responseEntity.getBody());
    }

    @Test
    void handleAccessDeniedException() {
        // Given
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access Denied");
        BDDMockito.when(webRequest.getDescription(false)).thenReturn(TEST_API_PATH);

        // When
        ResponseEntity<Object> responseEntity = restControllerAdvisor.handleAccessDeniedException(accessDeniedException,webRequest);

        // Then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        ErrorResponse expectedResponse = ErrorResponse.builder()
                .apiPath(TEST_API_PATH)
                .status(HttpStatus.FORBIDDEN)
                .errorMessage(accessDeniedException.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().ignoringFields(ERROR_TIME).isEqualTo(responseEntity.getBody());
    }

    @Test
    void handleMethodArgumentNotValid() {
        // Given
        BDDMockito.when(webRequest.getDescription(false)).thenReturn(TEST_API_PATH);
        BDDMockito.when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        // When
        ResponseEntity<Object> responseEntity = restControllerAdvisor.handleMethodArgumentNotValid(methodArgumentNotValidException, headers, HttpStatus.BAD_REQUEST,webRequest);

        // Then
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse expectedResponse = ErrorResponse.builder()
                .apiPath(TEST_API_PATH)
                .status(HttpStatus.BAD_REQUEST)
                .errorMessage(ExceptionUtility.ARGUMENT_NOT_VALID)
                .errorTime(LocalDateTime.now())
                .methodArgErrors(List.of())
                .build();
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().ignoringFields(ERROR_TIME).isEqualTo(responseEntity.getBody());
    }
}