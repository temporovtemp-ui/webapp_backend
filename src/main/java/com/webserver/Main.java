package com.webserver;

import com.webserver.endpoints.HelloWorldEndpoint;
import com.webserver.handlers.RoutingHandler;

public class Main {
    public static void main(String[] args) {
        int port = 8080;

        Router router = new Router();
        router.putEntry("/hello", HttpMethod.GET, new HelloWorldEndpoint());
        RoutingHandler routingHandler = new RoutingHandler(router);

        HttpServer server = new HttpServer(port, routingHandler);
        server.run();
    }
}
