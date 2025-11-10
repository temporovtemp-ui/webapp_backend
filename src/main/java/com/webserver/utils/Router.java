package com.webserver.utils;

import com.webserver.HttpMethod;
import com.webserver.handlers.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Router {
//    private record RouterKey(String path, HttpMethod method) {
//        @Override
//            public boolean equals(Object o) {
//                if (o == null || getClass() != o.getClass()) return false;
//                RouterKey routerKey = (RouterKey) o;
//                return Objects.equals(path, routerKey.path) && method == routerKey.method;
//            }
//
//    }

//    private final Map<RouterKey, Handler> routingTable = new HashMap<>();
    private final Map<String, Handler> routingTable = new HashMap<>();

//    public void putEntry(String path, HttpMethod method, Handler handler) {
//        PathValidator.validatePath(path);
//        RouterKey key = new RouterKey(path, method);
//        routingTable.put(key, handler);
//    }

    public void putEntry(String path, Handler handler) {
        PathValidator.validatePath(path);
        if (handler == null)
            throw new IllegalArgumentException("handler must not be null");
        routingTable.put(path, handler);
    }

//    public boolean removeEntry(String path, HttpMethod method) {
//        PathValidator.validatePath(path);
//        RouterKey key = new RouterKey(path, method);
//        return routingTable.remove(key) != null;
//    }

    public boolean removeEntry(String path) {
        PathValidator.validatePath(path);
        return routingTable.remove(path) != null;
    }

//    public Handler getHandler(String path, HttpMethod method) {
//        PathValidator.validatePath(path);
//        RouterKey key = new RouterKey(path, method);
//        return routingTable.get(key);
//    }

    public Handler getHandler(String path) {
        PathValidator.validatePath(path);
        return routingTable.get(path);
    }
}