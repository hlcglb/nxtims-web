package com.hyundaiuni.nxtims.framework.api;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Component
public class RestApiTemplate {
    private int DEFAULT_TIMEOUT = 3 * 60 * 1000;

    private RequestConfig config;

    public RestTemplate getRestTemplate() {
        this.config = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT).setConnectionRequestTimeout(
            DEFAULT_TIMEOUT).setSocketTimeout(DEFAULT_TIMEOUT).build();
        return new RestTemplate(getClientHttpRequestFactory());
    }

    public RestTemplate getRestTemplate(int timeout) {
        Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
        this.config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(
            timeout).setSocketTimeout(timeout).build();
        return new RestTemplate(getClientHttpRequestFactory());
    }
    
    public RestTemplate getRestTemplate(RequestConfig config) {
        Assert.notNull(config, "RequestConfig must not be null");
        this.config = config;
        return new RestTemplate(getClientHttpRequestFactory());
    }    

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(this.config).build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
