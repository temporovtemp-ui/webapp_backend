package com.webserver;

public class HttpVersionNotSupportedException extends Exception {
    public HttpVersionNotSupportedException() {
        super();
    }

    public HttpVersionNotSupportedException(String message) {
        super(message);
    }

    public HttpVersionNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpVersionNotSupportedException(Throwable cause) {
        super(cause);
    }
}
