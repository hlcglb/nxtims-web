package com.hyundaiuni.nxtims.service;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundaiuni.nxtims.exception.ServiceException;

public class RestApiResponseErrorHandler implements ResponseErrorHandler {
    private static final Log log = LogFactory.getLog(RestApiResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if(response.getStatusCode() != HttpStatus.OK) {
            log.debug("Status Code: " + response.getStatusCode());
            log.debug("Status Text: " + response.getStatusText());
            log.debug(response.getBody());

            return true;
        }
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if(response.getStatusCode() != HttpStatus.OK) {
            if(response.getBody() != null) {
                String responseBody = IOUtils.toString(response.getBody(), "UTF-8");

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    @SuppressWarnings("unchecked")
                    Map<String, String> error = objectMapper.readValue(responseBody, Map.class);

                    log.debug("Error Code: " + MapUtils.getString(error, "CODE"));
                    log.debug("Error Text: " + MapUtils.getString(error, "MESSAGE"));

                    String errorCode = MapUtils.getString(error, "CODE");
                    String errorMessage = MapUtils.getString(error, "MESSAGE");

                    throw new ServiceException(errorCode, errorMessage);
                }
                catch(JsonParseException e) {
                    log.error("RestApiResponseErrorHandler Exception : ", e);
                }
            }
        }
    }
}
