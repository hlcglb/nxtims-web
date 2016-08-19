package com.hyundaiuni.nxtims.exception;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -9061697529915101565L;

    private String code = null;

    public ServiceException(String code, String message) {
        this(code, message, null);
    }

    public ServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
