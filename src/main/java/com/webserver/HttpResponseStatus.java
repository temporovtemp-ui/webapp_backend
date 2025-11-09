package com.webserver;

public enum HttpResponseStatus {
    OK_200("200 OK"),
    CREATED_201("201 Created"),
    NO_CONTENT_204("204 No Content"),
    BAD_REQUEST_400("400 Bad Request"),
    UNAUTHORIZED_401("401 Unauthorized"),
    FORBIDDEN_403("403 Forbidden"),
    NOT_FOUND_404("404 Not Found"),
    CONFLICT_409("409 Conflict"),
    UNPROCESSABLE_ENTITY_422("422 Unprocessable Entity"),
    INTERNAL_SERVER_ERROR_500("500 Internal Server Error"),
    HTTP_VERSION_NOT_SUPPORTED_505("505 HTTP Version Not Supported");

    private final String displayName;

    HttpResponseStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
