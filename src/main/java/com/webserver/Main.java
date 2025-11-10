package com.webserver;

import com.webserver.endpoints.*;
import com.webserver.handlers.CorsHandler;
import com.webserver.handlers.RoutingHandler;
import com.webserver.handlers.ViewHandler;
import com.webserver.utils.DummyUserDatabase;
import com.webserver.utils.Router;

public class Main {
    public static void main(String[] args) {
        final int PORT = 8080;
        final String[] ALLOWED_CORS_ORIGINS = new String[]{"http://localhost:8000",};
        final String[] ALLOWED_CORS_HEADERS = new String[]{"Content-Type", "Authorization"};

        Router router = new Router();

        ViewHandler helloWorldViewHandler = new ViewHandler();
        helloWorldViewHandler.setMethodHandler(HttpMethod.GET, new HelloWorldEndpoint());
        CorsHandler helloWorldCorsHandler = new CorsHandler(ALLOWED_CORS_ORIGINS, ALLOWED_CORS_HEADERS, helloWorldViewHandler);
        router.putEntry("/hello", helloWorldCorsHandler);

        DummyUserDatabase db = new DummyUserDatabase();

        ViewHandler signUpViewHandler = new ViewHandler();
        signUpViewHandler.setMethodHandler(HttpMethod.POST, new SignUpEndpoint(db));
        CorsHandler signUpCorsHandler = new CorsHandler(ALLOWED_CORS_ORIGINS, ALLOWED_CORS_HEADERS, signUpViewHandler);
        router.putEntry("/register", signUpCorsHandler);

        ViewHandler signInViewHandler = new ViewHandler();
        signInViewHandler.setMethodHandler(HttpMethod.POST, new SignInEndpoint(db));
        CorsHandler signInCorsHandler = new CorsHandler(ALLOWED_CORS_ORIGINS, ALLOWED_CORS_HEADERS, signInViewHandler);
        router.putEntry("/login", signInCorsHandler);

        ViewHandler profileViewHandler = new ViewHandler();
        profileViewHandler.setMethodHandler(HttpMethod.GET, new ReadUserEndpoint(db));
        CorsHandler profileCorsHandler = new CorsHandler(ALLOWED_CORS_ORIGINS, ALLOWED_CORS_HEADERS, profileViewHandler);
        router.putEntry("/profile", profileCorsHandler);

        ViewHandler deleteProfileViewHandler = new ViewHandler();
        deleteProfileViewHandler.setMethodHandler(HttpMethod.POST, new DeleteUserEndpoint(db));
        CorsHandler deleteProfileCorsHandler = new CorsHandler(ALLOWED_CORS_ORIGINS, ALLOWED_CORS_HEADERS, deleteProfileViewHandler);
        router.putEntry("/profile/delete", deleteProfileCorsHandler);

        RoutingHandler routingHandler = new RoutingHandler(router);

        HttpServer server = new HttpServer(PORT, routingHandler);

        server.run();
    }
}
