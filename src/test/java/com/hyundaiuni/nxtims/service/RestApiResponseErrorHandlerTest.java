package com.hyundaiuni.nxtims.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundaiuni.nxtims.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestApiResponseErrorHandlerTest {
    private RestApiResponseErrorHandler errorHandler;

    @Before
    public void setUp() {
        errorHandler = new RestApiResponseErrorHandler();
    }

    @Test
    public void testHasError() {
        Exception ex = null;
        
        try {
            String errorJson = "test";

            byte[] body = errorJson.getBytes("UTF-8");

            MockClientHttpResponse httpResponse = new MockClientHttpResponse(body, HttpStatus.BAD_REQUEST);

            boolean hasError = errorHandler.hasError(httpResponse);
            
            assertThat(hasError).isTrue();
            
            httpResponse = new MockClientHttpResponse(body, HttpStatus.OK);

            hasError = errorHandler.hasError(httpResponse);
            
            assertThat(hasError).isFalse();            
        }
        catch(Exception e) {
            ex = e;
        }
        
        assertEquals(null, ex);
    }
    
    @Test
    public void testHandleError() {
        try {
            Map<String, String> error = new HashMap<>();
            error.put("CODE", "999");
            error.put("MESSAGE", "TEST");

            ObjectMapper objectMapper = new ObjectMapper();
            String errorJson = objectMapper.writeValueAsString(error);

            byte[] body = errorJson.getBytes("UTF-8");

            MockClientHttpResponse httpResponse = new MockClientHttpResponse(body, HttpStatus.BAD_REQUEST);

            errorHandler.handleError(httpResponse);
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(ServiceException.class);
        }
    }
}
