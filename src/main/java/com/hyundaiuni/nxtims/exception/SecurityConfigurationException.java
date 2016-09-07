package com.hyundaiuni.nxtims.exception;

public class SecurityConfigurationException extends Exception {
    private static final long serialVersionUID = -2426643690640265612L;

    public SecurityConfigurationException() {

    }

    public SecurityConfigurationException(String message) {
        super(message);
    }

    public SecurityConfigurationException(Throwable cause) {
        super(cause);
    }

    public SecurityConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
