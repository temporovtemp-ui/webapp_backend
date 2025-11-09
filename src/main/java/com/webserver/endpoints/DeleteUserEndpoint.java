package com.webserver.endpoints;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;
import com.webserver.handlers.AuthorizationHandler;
import com.webserver.handlers.Handler;
import com.webserver.utils.DummyUserDatabase;
import com.webserver.utils.User;

public class DeleteUserEndpoint extends Handler {
    private final DummyUserDatabase db;
    private final AuthorizationHandler authorizationHandler;

    public DeleteUserEndpoint(DummyUserDatabase db) {
        this.db = db;
        authorizationHandler = new AuthorizationHandler(db);
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Processing delete user request...");

        authorizationHandler.handle(request, response);
        User readUser = authorizationHandler.getReadUser();
        if (readUser == null)
            return;

        System.out.println("User read");
        db.removeUser(readUser.getUsername());
        System.out.println("User deleted");

        response.httpResponseStatus = HttpResponseStatus.NO_CONTENT_204;
        response.httpVersion="HTTP/1.1";
        response.headers.put("Content-Length", "0");
    }
}
