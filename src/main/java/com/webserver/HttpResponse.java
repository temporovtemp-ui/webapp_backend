package com.webserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    public String httpVersion;
    public HttpResponseStatus httpResponseStatus;
    public final Map<String, String> headers = new HashMap<>();
    public byte[] body;

    @Override
    public String toString() {
        return "HttpResponse{" +
                "httpVersion='" + httpVersion + '\'' +
                ", httpResponseStatus=" + httpResponseStatus +
                ", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
