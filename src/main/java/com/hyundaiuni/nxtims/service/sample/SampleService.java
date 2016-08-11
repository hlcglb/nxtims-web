package com.hyundaiuni.nxtims.service.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SampleService {
    @Value("${system.api.server.url}")
    private String apiServerUrl;
    private String apiUrl = "/api/v1/sample";

    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String id) {
        String url = apiServerUrl + apiUrl + "/" + id;
        try (InputStream stream = new URL(url).openConnection().getInputStream()) {
            return new ObjectMapper().readValue(stream, Map.class);
        }
        catch(IOException e) {
            e.printStackTrace();

            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            return map;
        }
    }
}
