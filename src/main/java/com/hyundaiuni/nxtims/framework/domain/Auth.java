package com.hyundaiuni.nxtims.framework.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Auth implements Serializable {
    private static final long serialVersionUID = 6430679059199263863L;
    
    @JsonProperty(value = "AUTH_ID")
    private String authId = null;

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }
}
