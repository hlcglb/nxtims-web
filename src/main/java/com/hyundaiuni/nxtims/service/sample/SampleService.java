package com.hyundaiuni.nxtims.service.sample;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hyundaiuni.nxtims.framework.api.RestApiTemplate;

@Component
public class SampleService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/nxtims/v1/sample";

    @Autowired
    private RestApiTemplate apiTemplate;

    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String id) {
        String resourceUrl = apiServerUrl + apiUrl;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return apiTemplate.getRestTemplate().getForObject(resourceUrl + "/{id}", Map.class, params);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getWithTimeout(String id, int timeout) {
        String resourceUrl = apiServerUrl + apiUrl;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return apiTemplate.getRestTemplate(timeout).getForObject(resourceUrl + "/{id}", Map.class, params);
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getWithRequestConfig(String id) {
        String resourceUrl = apiServerUrl + apiUrl;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        RequestConfig config = RequestConfig.custom().setConnectTimeout(1 * 60 * 1000).setConnectionRequestTimeout(
            1 * 60 * 1000).setSocketTimeout(1 * 60 * 1000).build();

        return apiTemplate.getRestTemplate(config).getForObject(resourceUrl + "/{id}", Map.class, params);
    }

    public void insert(Map<String, Object> params) {
        String resourceUrl = apiServerUrl + apiUrl;
        apiTemplate.getRestTemplate().postForEntity(resourceUrl, params, null);
    }
}
