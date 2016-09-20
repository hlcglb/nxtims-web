package com.hyundaiuni.nxtims.exception;

public class SupportConfigurationException extends Exception {
    private static final long serialVersionUID = 5750094641486247121L;

    public SupportConfigurationException() {

    }

    public SupportConfigurationException(String message) {
        super(message);
    }

    public SupportConfigurationException(Throwable cause) {
        super(cause);
    }

    public SupportConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
