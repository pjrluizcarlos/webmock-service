package br.com.pjrluizcarlos.webmockservice.api.mock;

import br.com.pjrluizcarlos.webmockservice.business.mock.MockService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MockControllerTest {

    private static final String URI = "/test-1";
    private static final HttpStatus STATUS = HttpStatus.OK;
    private static final HttpMethod METHOD = HttpMethod.GET;
    private static final String RESPONSE = "{\"message\": \"Hello World!\"}";

    @InjectMocks private MockController controller;
    @Mock private MockService service;
    @Mock private HttpServletRequest request;

    @Test
    void givenAMockWithResponse_whenCallExecution_shouldExecuteMockWithJsonHeader() {
        br.com.pjrluizcarlos.webmockservice.business.mock.Mock model = br.com.pjrluizcarlos.webmockservice.business.mock.Mock.builder()
                .response(RESPONSE)
                .status(STATUS.value())
                .build();

        when(request.getRequestURI()).thenReturn(URI);
        when(request.getMethod()).thenReturn(METHOD.name());
        when(service.findByUriAndMethod(any(), any())).thenReturn(Optional.of(model));

        ResponseEntity<String> actual = controller.execute(request);

        assertEquals(STATUS, actual.getStatusCode());
        assertEquals(RESPONSE, actual.getBody());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, actual.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0));

        verify(request).getRequestURI();
        verify(request).getMethod();
        verify(service).findByUriAndMethod(URI, METHOD.name());
    }

    @Test
    void givenAMockWithoutResponse_whenCallExecution_shouldExecuteMockWithoutJsonHeader() {
        br.com.pjrluizcarlos.webmockservice.business.mock.Mock model = br.com.pjrluizcarlos.webmockservice.business.mock.Mock.builder()
                .status(STATUS.value())
                .response(null)
                .build();

        when(request.getRequestURI()).thenReturn(URI);
        when(request.getMethod()).thenReturn(METHOD.name());
        when(service.findByUriAndMethod(any(), any())).thenReturn(Optional.of(model));

        ResponseEntity<String> actual = controller.execute(request);

        assertEquals(STATUS, actual.getStatusCode());
        assertNull(actual.getBody());
        assertNull(actual.getHeaders().get(HttpHeaders.CONTENT_TYPE));

        verify(request).getRequestURI();
        verify(request).getMethod();
        verify(service).findByUriAndMethod(URI, METHOD.name());
    }

    @Test
    void giveNoMock_whenCallExecution_shouldReturnError() {
        when(request.getRequestURI()).thenReturn(URI);
        when(request.getMethod()).thenReturn(METHOD.name());
        when(service.findByUriAndMethod(any(), any())).thenReturn(Optional.empty());

        ResponseEntity<String> actual = controller.execute(request);

        assertEquals(HttpStatus.NOT_IMPLEMENTED, actual.getStatusCode());
        assertEquals("Mock execution not configured. [GET]: [/test-1].", actual.getBody());
        assertEquals(MediaType.TEXT_PLAIN_VALUE, actual.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0));

        verify(request).getRequestURI();
        verify(request).getMethod();
        verify(service).findByUriAndMethod(URI, METHOD.name());
    }

}