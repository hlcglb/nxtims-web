package com.hyundaiuni.nxtims.service.sample;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SampleService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/sample";

    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        String resourceUrl = apiServerUrl + apiUrl;

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> resultMap = restTemplate.getForObject(resourceUrl + "/{id}", Map.class, params);

        return resultMap;
    }

    public void insert(Map<String, Object> params) {
        String resourceUrl = apiServerUrl + apiUrl;
        
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(resourceUrl, params, null);
    }
}
