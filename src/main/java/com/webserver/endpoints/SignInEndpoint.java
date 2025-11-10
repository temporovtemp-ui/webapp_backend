package com.webserver.endpoints;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;
import com.webserver.handlers.Handler;
import com.webserver.utils.DummyUserDatabase;
import com.webserver.utils.JwtManager;
import com.webserver.utils.SignInFormValidator;
import com.webserver.utils.User;
import org.json.JSONObject;

public class SignInEndpoint extends Handler {
    private final DummyUserDatabase db;

    public SignInEndpoint(DummyUserDatabase db) {
        if (db == null)
            throw new IllegalArgumentException("db must not be null");
        this.db = db;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Processing sign in request...");

        JSONObject form;
        System.out.println("Attempting to extract JSON from the body...");
        try {
            form = new JSONObject(new String(request.body));
        } catch (RuntimeException e) {
            System.out.println("JSON extraction failed due to: " + e);
            response.httpResponseStatus = HttpResponseStatus.UNPROCESSABLE_ENTITY_422;
            response.httpVersion="HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Malformed sign in form\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            return;
        }
        System.out.println("JSON extracted from the body!");

        System.out.println("Validating sign in form...");
        try {
            SignInFormValidator.validateSignInForm(form);
        } catch (IllegalArgumentException e) {
            System.out.println("Validating sign in failed due to: " + e);
            response.httpResponseStatus = HttpResponseStatus.UNPROCESSABLE_ENTITY_422;
            response.httpVersion="HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = ("{\"error\": \"" + e.getMessage() + "\"}").getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            return;
        }
        System.out.println("Sign in form validated!");

        User foundUser;
        System.out.println("Reading user by provided username...");
        try {
            foundUser = db.readUser((String)form.get("username"));
        } catch (IllegalStateException e) {
            System.out.println("Reading user by provided username failed: " + e);
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion="HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Incorrect username or password\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            return;
        }
        System.out.println("Checking passwords...");
        if (!((String)form.get("password")).equals(foundUser.getPassword())) {
            System.out.println("Passwords don't match");
            response.httpResponseStatus = HttpResponseStatus.UNAUTHORIZED_401;
            response.httpVersion="HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Incorrect username or password\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            return;
        }
        System.out.println("Passwords match");

        response.httpResponseStatus = HttpResponseStatus.OK_200;
        response.httpVersion="HTTP/1.1";
        response.headers.put("Content-Type", "application/json");
        String body = "{\"jwt\": \"" + JwtManager.generateToken(foundUser) + "\"}";
        response.body = body.getBytes();
        response.headers.put("Content-Length", String.valueOf(response.body.length));
    }
}

//Access-Control-Allow-Origin
//access-control-allow-origin