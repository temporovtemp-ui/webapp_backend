package com.webserver;

public class HttpRequestFormatException extends RuntimeException {
    public HttpRequestFormatException() {
        super();
    }

    public HttpRequestFormatException(String message) {
        super(message);
    }

    public HttpRequestFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestFormatException(Throwable cause) {
        super(cause);
    }
}
