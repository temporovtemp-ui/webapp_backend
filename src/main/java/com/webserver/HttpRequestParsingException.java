package com.webserver;

public class HttpRequestParsingException extends Exception {
    public HttpRequestParsingException() {
        super();
    }

    public HttpRequestParsingException(String message) {
        super(message);
    }

    public HttpRequestParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestParsingException(Throwable cause) {
        super(cause);
    }
}
