package com.webserver.utils;

public class PathValidator {
    public static void validatePath(String path) {
        if (path == null)
            throw new IllegalArgumentException("Path should not be null");

        String pathOnly;
        String queryString = null;

        int queryIndex = path.indexOf('?');
        if (queryIndex >= 0) {
            pathOnly = path.substring(0, queryIndex);
            queryString = path.substring(queryIndex + 1);
        } else {
            pathOnly = path;
        }

        if (!pathOnly.matches("^(/[a-zA-Z0-9\\-._~%!$&'()*+,;=:@]*)+/?$"))
            throw new IllegalArgumentException("Invalid path format");

        if (queryString != null && !queryString.isEmpty()) {
            if (!queryString.matches("^([a-zA-Z0-9\\-._~%!$&'()*+,;=:@]+=[a-zA-Z0-9\\-._~%!$&'()*+,;=:@]*)(&[a-zA-Z0-9\\-._~%!$&'()*+,;=:@]+=[a-zA-Z0-9\\-._~%!$&'()*+,;=:@]*)*$")) {
                throw new IllegalArgumentException("Invalid query parameter format - parameters must have values");
            }
        }
    }
}
