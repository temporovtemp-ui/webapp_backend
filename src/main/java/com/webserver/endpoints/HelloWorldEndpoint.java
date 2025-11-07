package com.webserver.endpoints;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;
import com.webserver.handlers.Handler;

public class HelloWorldEndpoint extends Handler {

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        response.httpVersion = "HTTP/1.1";
        response.httpResponseStatus = HttpResponseStatus.OK_200;
        response.headers.put("Content-Type", "text/plain");
        response.headers.put("Connection", "close");
        response.body = "Hello, World!".getBytes();
        response.headers.put("Content-Length", String.valueOf(response.body.length));
    }
}
