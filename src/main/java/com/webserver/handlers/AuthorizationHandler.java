package com.webserver.handlers;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;
import com.webserver.utils.DummyUserDatabase;
import com.webserver.utils.JwtManager;
import com.webserver.utils.User;
import org.json.JSONObject;

import java.time.Instant;

public class AuthorizationHandler extends Handler {
    private final DummyUserDatabase db;
    private User readUser = null;

    public AuthorizationHandler(DummyUserDatabase db) {
        this.db = db;
    }

    public User getReadUser() {
        return readUser;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Authorizing client...");

        System.out.println("Checking if Authorization header is in place and not malformed...");
        String authorization = request.headers.getOrDefault("Authorization", null);
        if (authorization == null ||
                !authorization.matches("^Bearer\\s+[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$")) {
            System.out.println("It isn't :(");
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion = "HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Authorization header is missing or malformed\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            readUser = null;
            return;
        }
        System.out.println("It is :)");

        String token = authorization.substring(7);
        System.out.println("Extracted JWT: " + token);
        System.out.println("Validating JWT...");
        if (!JwtManager.validateToken(token)) {
            System.out.println("JWT is invalid");
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion = "HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Invalid token\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            readUser = null;
            return;
        }
        System.out.println("JWT is valid");

        JSONObject payload = JwtManager.getPayload(token);
        System.out.println("Extracted payload: " + payload);

        System.out.println("Checking if payload contains iat...");
        if (!payload.has("iat")) {
            System.out.println("It doesn't :(");
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion = "HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Token payload does not contain iat\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            readUser = null;
            return;
        }
        long iat = payload.getLong("iat");

        System.out.println("Checking if payload contains exp...");
        if (!payload.has("exp")) {
            System.out.println("It doesn't :(");
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion = "HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Token payload does not contain exp\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            readUser = null;
            return;
        }
        long exp = payload.getLong("exp");

        System.out.println("Checking if token is active");
        long now = Instant.now().getEpochSecond();
        if (now > exp) {
            System.out.println("It's not :(");
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion = "HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Token expired\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            readUser = null;
            return;
        }

        String username = (String) payload.get("username");
        System.out.println("Checking if payload contains username...");
        if (username == null) {
            System.out.println("It doesn't :(");
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion = "HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Token payload does not contain username\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            readUser = null;
            return;
        }
        System.out.println("It does");

        System.out.println("Attempting to read claimed username from db...");
        try {
            readUser = db.readUser(username);
        } catch (IllegalStateException e) {
            System.out.println("Failed!");
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion = "HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Claimed user does not exist\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            readUser = null;
            return;
        }
        System.out.println("Read user: " + readUser);
    }
}
