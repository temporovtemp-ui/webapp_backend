package com.webserver;

import com.webserver.endpoints.*;
import com.webserver.handlers.RoutingHandler;
import com.webserver.utils.DummyUserDatabase;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        Router router = new Router();
        router.putEntry("/hello", HttpMethod.GET, new HelloWorldEndpoint());
        DummyUserDatabase db = new DummyUserDatabase();
        router.putEntry("/users", HttpMethod.POST, new SignUpEndpoint(db));
        router.putEntry("/login", HttpMethod.POST, new SignInEndpoint(db));
        router.putEntry("/profile", HttpMethod.GET, new ReadUserEndpoint(db));
        router.putEntry("/profile/delete", HttpMethod.POST, new DeleteUserEndpoint(db));
        RoutingHandler routingHandler = new RoutingHandler(router);
        HttpServer server = new HttpServer(port, routingHandler);
        server.run();
    }
}
