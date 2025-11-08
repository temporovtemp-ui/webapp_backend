package com.webserver;

import com.webserver.handlers.Handler;
import com.webserver.utils.PathValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Router {
    private record RouterKey(String path, HttpMethod method) {
        @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                RouterKey routerKey = (RouterKey) o;
                return Objects.equals(path, routerKey.path) && method == routerKey.method;
            }

    }

    private final Map<RouterKey, Handler> routingTable = new HashMap<>();

    public void putEntry(String path, HttpMethod method, Handler handler) {
        PathValidator.validatePath(path);
        RouterKey key = new RouterKey(path, method);
        routingTable.put(key, handler);
    }

    public boolean removeEntry(String path, HttpMethod method) {
        PathValidator.validatePath(path);
        RouterKey key = new RouterKey(path, method);
        return routingTable.remove(key) != null;
    }

    public Handler getHandler(String path, HttpMethod method) {
        PathValidator.validatePath(path);
        RouterKey key = new RouterKey(path, method);
        return routingTable.get(key);
    }
}