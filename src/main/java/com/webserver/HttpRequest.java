package com.webserver;

import com.webserver.utils.PathValidator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public HttpMethod method;
    private String path;
    public final Map<String, String> queryParameters = new HashMap<>();
    public String httpVersion;
    public final Map<String, String> headers = new HashMap<>();
    public byte[] body;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        try {
            PathValidator.validatePath(path);
        } catch (IllegalArgumentException e) {
            throw new HttpRequestFormatException("Invalid path format");
        }
        this.path = path;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", queryParameters=" + queryParameters +
                ", httpVersion='" + httpVersion + '\'' +
                ", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
