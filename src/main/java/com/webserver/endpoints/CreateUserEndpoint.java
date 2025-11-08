package com.webserver.endpoints;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.handlers.Handler;

import org.json.JSONObject;

public class CreateUserEndpoint extends Handler {

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        JSONObject form = new JSONObject(new String(request.body));

    }
}
