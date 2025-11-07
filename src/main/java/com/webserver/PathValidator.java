package com.webserver;

public class PathValidator {
    public static void validatePath(String path) {
        if (path == null)
            throw new IllegalArgumentException("Path should not be null");
        if (!path.matches("^(/[a-zA-Z0-9\\-._~%!$&'()*+,;=:@]*)+/?$"))
            throw new IllegalArgumentException("Invalid path format");
    }
}
