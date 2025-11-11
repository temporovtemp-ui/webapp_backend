package com.webserver.endpoints;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;
import com.webserver.handlers.Handler;

import com.webserver.utils.DummyUserDatabase;
import com.webserver.utils.SignUpFormValidator;
import com.webserver.utils.User;
import com.webserver.utils.UserSerializer;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class SignUpEndpoint extends Handler {
    private final DummyUserDatabase db;

    public SignUpEndpoint(DummyUserDatabase db) {
        if (db == null)
            throw new IllegalArgumentException("db must not be null");
        this.db = db;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Processing sign up request...");
        JSONObject form;
        System.out.println("Attempting to extract JSON form from body...");
        try {
            form = new JSONObject(new String(request.body));
        } catch (RuntimeException e) {
            System.out.println("JSON extraction failed due to: " + e);
            response.httpResponseStatus = HttpResponseStatus.UNPROCESSABLE_ENTITY_422;
            response.httpVersion="HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = "{\"error\": \"Malformed sign up form\"}".getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            return;
        }
        System.out.println("JSON extraction completed!");

        System.out.println("Validating sign up form...");
        try {
            SignUpFormValidator.validateSignUpForm(form);
        } catch (IllegalArgumentException e) {
            System.out.println("Validating sign up failed due to: " + e);
            response.httpResponseStatus = HttpResponseStatus.UNPROCESSABLE_ENTITY_422;
            response.httpVersion="HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = ("{\"error\": \"" + e.getMessage() + "\"}").getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            return;
        }
        System.out.println("Validated sign up form successfully!");


        User newUser = new User((String)form.get("username"), (String)form.get("password"));
        System.out.println("Attempting to insert new user: " + newUser);
        try {
            db.insertUser(newUser);
        } catch (IllegalStateException e) {
            System.out.println("New user insertion failed due to: " + e);
            response.httpResponseStatus = HttpResponseStatus.CONFLICT_409;
            response.httpVersion="HTTP/1.1";
            response.headers.put("Content-Type", "application/json");
            response.body = ("{\"error\": \"" + e.getMessage() + "\"}").getBytes();
            response.headers.put("Content-Length", String.valueOf(response.body.length));
            return;
        }
        System.out.println("Inserted new user successfully!");

        response.httpResponseStatus = HttpResponseStatus.CREATED_201;
        //response.httpResponseStatus = HttpResponseStatus.BAD_REQUEST_400;
        response.httpVersion="HTTP/1.1";
        response.headers.put("Content-Type", "application/json");
        response.body = UserSerializer.serializeUser(newUser).toString().getBytes();
        response.headers.put("Content-Length", String.valueOf(response.body.length));
    }
}
