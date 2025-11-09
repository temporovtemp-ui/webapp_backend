package com.webserver.endpoints;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;
import com.webserver.handlers.AuthorizationHandler;
import com.webserver.handlers.Handler;
import com.webserver.utils.DummyUserDatabase;
import com.webserver.utils.JwtManager;
import com.webserver.utils.User;
import com.webserver.utils.UserSerializer;

public class ReadUserEndpoint extends Handler {
    private final AuthorizationHandler authorizationHandler;
    private final DummyUserDatabase db;

    public ReadUserEndpoint(DummyUserDatabase db) {
        if (db == null)
            throw new IllegalArgumentException("db must not be null");
        this.db = db;
        authorizationHandler = new AuthorizationHandler(db);
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Reading user...");
        authorizationHandler.handle(request, response);
        User readUser = authorizationHandler.getReadUser();
        if (readUser == null)
            return;
        System.out.println("User read");
        response.httpResponseStatus = HttpResponseStatus.OK_200;
        response.httpVersion="HTTP/1.1";
        response.headers.put("Content-Type", "application/json");
        response.body = UserSerializer.serializeUser(readUser).toString().getBytes();
        response.headers.put("Content-Length", String.valueOf(response.body.length));
    }
}
