package com.webserver;

public class HttpResponseSerializationException extends Exception {
    public HttpResponseSerializationException() {
        super();
    }

    public HttpResponseSerializationException(String message) {
        super(message);
    }

    public HttpResponseSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResponseSerializationException(Throwable cause) {
        super(cause);
    }
}
