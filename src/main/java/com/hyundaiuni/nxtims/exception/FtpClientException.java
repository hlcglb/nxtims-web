package com.hyundaiuni.nxtims.exception;

public class FtpClientException extends Exception {
    private static final long serialVersionUID = 5750094641486247121L;

    public FtpClientException() {

    }

    public FtpClientException(String message) {
        super(message);
    }

    public FtpClientException(Throwable cause) {
        super(cause);
    }

    public FtpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
