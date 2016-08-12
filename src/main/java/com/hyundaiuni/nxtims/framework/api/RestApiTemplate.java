package com.hyundaiuni.nxtims.framework.api;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestApiTemplate {
    private RestTemplate template;
    private int timeout = 3 * 60 * 1000;

    public RestApiTemplate() {
        this.template = new RestTemplate(getClientHttpRequestFactory());
    }

    public RestTemplate getRestTemplate() {
        return template;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(
            timeout).setSocketTimeout(timeout).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        return new HttpComponentsClientHttpRequestFactory(client);
    }
}
