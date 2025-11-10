package com.webserver.handlers;

import com.webserver.HttpMethod;
import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;

import java.util.*;

public class CorsHandler extends Handler {
    private final Set<String> allowedOrigins;
    private final Set<String> allowedHeaders;
    private final ViewHandler viewHandler;

    public CorsHandler(String[] allowedOrigins, String[] allowedHeaders, ViewHandler viewHandler) {
        validateAllowedOrigins(allowedOrigins);
        if (allowedHeaders == null)
            throw new IllegalArgumentException("allowedHeaders must not be null");
        if (viewHandler == null)
            throw new IllegalArgumentException("viewHandler must not be null");

        this.allowedOrigins = new HashSet<>(List.of(allowedOrigins));
        this.allowedHeaders = new HashSet<>(List.of(allowedHeaders));
        this.viewHandler = viewHandler;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Processing CORS...");

        String origin = request.headers.getOrDefault("Origin", null);
        if (!allowedOrigins.contains(origin)) {
            System.out.println("Origin is not in allowed origins list!");
            response.httpResponseStatus = HttpResponseStatus.FORBIDDEN_403;
            response.httpVersion = "HTTP/1.1";
            String value = String.join(", ", allowedOrigins);
            response.headers.put("Access-Control-Allow-Origin", value);
            response.body = "CORS policy: Origin not allowed".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            return;
        }
        response.headers.put("Access-Control-Allow-Origin", origin);
        System.out.println("Origin allowed");

        if (request.method == HttpMethod.OPTIONS) {
            System.out.println("Responding to OPTIONS request!");
            response.httpResponseStatus = HttpResponseStatus.NO_CONTENT_204;
            response.httpVersion = "HTTP/1.1";
            String value = String.join(", ", Arrays.stream(viewHandler.implementedMethods()).map(String::valueOf).toArray(String[]::new));
            response.headers.put("Access-Control-Allow-Methods", value);
            value = String.join(", ", allowedHeaders);
            response.headers.put("Access-Control-Allow-Headers", value);
            return;
        }

        viewHandler.handle(request, response);
    }

    private void validateAllowedOrigins(String[] allowedOrigins) {
        if (allowedOrigins == null)
            throw new IllegalArgumentException("allowedOrigins must not be null");
        for (String origin: allowedOrigins)
            if (!origin.matches("^https?://([a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)(?::(\\d{1,5}))?$"))
                throw new IllegalArgumentException("Malformed origin: '" + origin + "'");
    }
}
