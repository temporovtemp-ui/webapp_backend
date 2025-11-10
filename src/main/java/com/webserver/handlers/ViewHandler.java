package com.webserver.handlers;

import com.webserver.HttpMethod;
import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class ViewHandler extends Handler {
    private final Map<HttpMethod, Handler> methodHandlerMap = new HashMap<>();

    public HttpMethod[] implementedMethods() {
        return methodHandlerMap.keySet().toArray(new HttpMethod[0]);
    }

    public void setMethodHandler(HttpMethod method, Handler handler) {
        validateMethod(method);
        if (handler == null)
            throw new IllegalArgumentException("handler must not be null");
        methodHandlerMap.put(method, handler);
    }

    public boolean resetMethodHandler(HttpMethod method) {
        validateMethod(method);
        return methodHandlerMap.remove(method) != null;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Dispatching by HTTP method...");
        Handler handler = methodHandlerMap.getOrDefault(request.method, null);
        if (handler != null) {
            System.out.println("Dispatching to " + request.method + " handler...");
            handler.handle(request, response);
            return;
        }

        System.out.println("No handler found for " + request.method);
        response.httpResponseStatus = HttpResponseStatus.METHOD_NOT_ALLOWED_405;
        response.httpVersion = "HTTP/1.1";
        response.headers.put("Content-Length", "0");
    }

    private void validateMethod(HttpMethod method) {
        if (method == null)
            throw new IllegalArgumentException("method must not be null");
    }
}
