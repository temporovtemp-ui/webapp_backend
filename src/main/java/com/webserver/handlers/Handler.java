package com.webserver.handlers;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;

public abstract class Handler {
    public abstract void handle(HttpRequest request, HttpResponse response);
}
