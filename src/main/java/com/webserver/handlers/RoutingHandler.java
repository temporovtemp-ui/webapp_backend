package com.webserver.handlers;

import com.webserver.HttpRequest;
import com.webserver.HttpResponse;
import com.webserver.HttpResponseStatus;
import com.webserver.utils.Router;

public class RoutingHandler extends Handler{
    private Router router;

    public RoutingHandler(Router router) {
        validateRouter(router);
        this.router = router;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        validateRouter(router);
        this.router = router;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        System.out.println("Routing request...");
        Handler handler = router.getHandler(request.getPath());
        if (handler == null) {
            System.out.println("No handler found for path: " + request.getPath());
            response.httpVersion = "HTTP/1.1";
            response.httpResponseStatus = HttpResponseStatus.NOT_FOUND_404;
            response.headers.put("Content-Length", "0");
            return;
        }
        System.out.println("Found handler: " + handler);
        handler.handle(request, response);
    }

    private void validateRouter(Router router) {
        if (router == null)
            throw new IllegalArgumentException("router shall not be null");
    }
}
