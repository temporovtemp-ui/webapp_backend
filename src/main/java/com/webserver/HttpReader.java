package com.webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HttpReader implements Closeable {
    InputStream inputStream;
    BufferedReader reader;

    public HttpReader(Socket clientSocket) throws IOException {
        if (clientSocket == null) {
            throw new IllegalArgumentException("Client socket cannot be null");
        }
        inputStream = clientSocket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public HttpRequest readRequest() throws IOException, HttpRequestParsingException {
        HttpRequest httpRequest = new HttpRequest();
        System.out.println("Reading headers...");
        int contentLength = readHeaders(httpRequest);
        System.out.println("Headers read. Content-Length: " + contentLength);
        if (contentLength > 0)
            readBody(httpRequest, contentLength);
        return httpRequest;
    }

    @Override
    public void close() throws IOException {
        reader.close();
        inputStream.close();
    }

    private int readHeaders(HttpRequest httpRequest) throws IOException, HttpRequestParsingException {
        List<String> headerLines = new ArrayList<>();
        while (true) {
            String line = reader.readLine();
            System.out.println("Line read: '" + line + "'");
            if (line == null) {
                throw new HttpRequestParsingException("Connection closed before completing headers section");
            }
            if (line.isEmpty()) {
                break;
            }
            headerLines.add(line);
        }

        if (headerLines.isEmpty())
            throw new HttpRequestParsingException("Request headers are empty");

        String methodLine = headerLines.getFirst();
        String[] methodLineParts = methodLine.split(" ");
        if (methodLineParts.length != 3)
            throw new HttpRequestParsingException("Request method line does not contain 3 tokens: " + methodLine);


        try {
            httpRequest.method = HttpMethod.valueOf(methodLineParts[0]);
        } catch (IllegalArgumentException e) {
            throw new HttpRequestParsingException("Invalid HTTP method: " + methodLineParts[0]);
        }

        try {
            httpRequest.setPath(methodLineParts[1]);
        } catch (HttpRequestFormatException e) {
            throw new HttpRequestParsingException("Invalid HTTP path: " + methodLineParts[1]);
        }

        httpRequest.httpVersion = methodLineParts[2];

        for (int i = 1; i < headerLines.size(); i++) {
            String headerLine = headerLines.get(i);
            String[] headerLineParts = headerLine.split(":", 2);
            String headerName = headerLineParts[0].trim();
            String headerValue = headerLineParts[1].trim();
            httpRequest.headers.put(headerName, headerValue);
        }

        int contentLength = 0;
        if (httpRequest.headers.containsKey("Content-Length")) {
            try {
                contentLength = Integer.parseInt(httpRequest.headers.get("Content-Length"));
                if (contentLength < 0) {
                    throw new HttpRequestParsingException("Invalid Content-Length: " + contentLength);
                }
            } catch (NumberFormatException e) {
                throw new HttpRequestParsingException("Invalid Content-Length format: " + httpRequest.headers.get("Content-Length"));
            }
        }
        return contentLength;
    }

    private void readBody(HttpRequest httpRequest, int contentLength) throws IOException {
        httpRequest.body = inputStream.readNBytes(contentLength);
    }
}
