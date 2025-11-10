package com.webserver;

import com.webserver.endpoints.*;
import com.webserver.handlers.RoutingHandler;
import com.webserver.handlers.ViewHandler;
import com.webserver.utils.DummyUserDatabase;
import com.webserver.utils.Router;

public class Main {
    public static void main(String[] args) {
        int port = 8080;

        Router router = new Router();

        ViewHandler helloWorldViewHandler = new ViewHandler();
        helloWorldViewHandler.setMethodHandler(HttpMethod.GET, new HelloWorldEndpoint());
        router.putEntry("/hello", helloWorldViewHandler);

        DummyUserDatabase db = new DummyUserDatabase();

        ViewHandler signUpViewHandler = new ViewHandler();
        signUpViewHandler.setMethodHandler(HttpMethod.POST, new SignUpEndpoint(db));
        router.putEntry("/register", signUpViewHandler);

        ViewHandler signInViewHandler = new ViewHandler();
        signInViewHandler.setMethodHandler(HttpMethod.POST, new SignInEndpoint(db));
        router.putEntry("/login", signInViewHandler);

        ViewHandler profileViewHandler = new ViewHandler();
        profileViewHandler.setMethodHandler(HttpMethod.GET, new ReadUserEndpoint(db));
        router.putEntry("/profile", profileViewHandler);

        ViewHandler deleteProfileViewHandler = new ViewHandler();
        deleteProfileViewHandler.setMethodHandler(HttpMethod.POST, new DeleteUserEndpoint(db));
        router.putEntry("/profile/delete", deleteProfileViewHandler);

        RoutingHandler routingHandler = new RoutingHandler(router);

        HttpServer server = new HttpServer(port, routingHandler);

        server.run();
    }
}
