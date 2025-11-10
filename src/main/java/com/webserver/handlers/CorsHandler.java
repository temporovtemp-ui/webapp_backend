package com.webserver.handlers;

import com.webserver.HttpMethod;
import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;

import java.util.*;

public class CorsHandler extends Handler {
    private Set<String> allowedOrigins;
    private Set<String> allowedHeaders;
    private ViewHandler viewHandler;

    public CorsHandler(String[] allowedOrigins, String[] allowedHeaders, ViewHandler viewHandler) {
        validateAllowedOrigins(allowedOrigins);
        if (allowedHeaders == null)
            throw new IllegalArgumentException("allowedHeaders must not be null");
        if (this.viewHandler == null)
            throw new IllegalArgumentException("nextHandler must not be null");

        this.allowedOrigins = new HashSet<>(List.of(allowedOrigins));
        this.allowedHeaders = new HashSet<>(List.of(allowedHeaders));
        this.viewHandler = viewHandler;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Processing CORS...");

        if (request.method == HttpMethod.OPTIONS) {
            System.out.println("Responding to OPTIONS request!");
            response.httpResponseStatus = HttpResponseStatus.NO_CONTENT_204;
            response.httpVersion = "HTTP/1.1";
            String value = String.join(", ", allowedOrigins);
            response.headers.put("Access-Control-Allow-Origin", value);
            value = String.join(", ", Arrays.stream(viewHandler.implementedMethods()).map(String::valueOf).toArray(String[]::new));
            response.headers.put("Access-Control-Allow-Methods", value);
            value = String.join(", ", allowedHeaders);
            response.headers.put("Access-Control-Allow-Headers", value);
            return;
        }

        System.out.println("Checking CORS...");

        String origin = request.headers.get("Origin");
        if (!allowedOrigins.contains(origin)) {

        }
    }

    private void validateAllowedOrigins(String[] allowedOrigins) {
        if (allowedOrigins == null)
            throw new IllegalArgumentException("allowedOrigins must not be null");
        for (String origin: allowedOrigins)
            if (!origin.matches("^https?://([a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)(?::(\\d{1,5}))?$"))
                throw new IllegalArgumentException("Malformed origin: '" + origin + "'");
    }
}
