package com.webserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public HttpMethod method;
    private String path;
    public String httpVersion;
    public final Map<String, String> headers = new HashMap<>();
    public byte[] body;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (!path.matches("^(/[a-zA-Z0-9\\-._~%!$&'()*+,;=:@]*)+/?$"))
            throw new HttpRequestFormatException("Invalid path format");
        this.path = path;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
