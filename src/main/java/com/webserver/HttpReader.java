//package com.webserver;
//
//import com.webserver.utils.PathValidator;
//
//import java.io.*;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class HttpReader implements Closeable {
//    InputStream inputStream;
//    BufferedReader reader;
//
//    public HttpReader(Socket clientSocket) throws IOException {
//        if (clientSocket == null) {
//            throw new IllegalArgumentException("Client socket cannot be null");
//        }
//        inputStream = clientSocket.getInputStream();
//        reader = new BufferedReader(new InputStreamReader(inputStream));
//    }
//
//    public HttpRequest readRequest() throws IOException, HttpRequestParsingException {
//        HttpRequest httpRequest = new HttpRequest();
//        System.out.println("Reading headers...");
//        int contentLength = readHeaders(httpRequest);
//        System.out.println("Headers read. Content-Length: " + contentLength);
//        System.out.println("Reading body...");
//        readBody(httpRequest, contentLength);
//        System.out.println("Body read!");
//        return httpRequest;
//    }
//
//    @Override
//    public void close() throws IOException {
//        reader.close();
//        inputStream.close();
//    }
//
//    private int readHeaders(HttpRequest httpRequest) throws IOException, HttpRequestParsingException {
//        List<String> headerLines = new ArrayList<>();
//        while (true) {
//            String line = reader.readLine();
//            System.out.println("Line read: '" + line + "'");
//            if (line == null) {
//                throw new HttpRequestParsingException("Connection closed before completing headers section");
//            }
//            if (line.isEmpty()) {
//                break;
//            }
//            headerLines.add(line);
//        }
//
//        if (headerLines.isEmpty())
//            throw new HttpRequestParsingException("Request headers are empty");
//
//        String methodLine = headerLines.get(0);
//        String[] methodLineParts = methodLine.split(" ");
//        if (methodLineParts.length != 3)
//            throw new HttpRequestParsingException("Request method line does not contain 3 tokens: " + methodLine);
//
//
//        try {
//            httpRequest.method = HttpMethod.valueOf(methodLineParts[0]);
//        } catch (IllegalArgumentException e) {
//            throw new HttpRequestParsingException("Invalid HTTP method: " + methodLineParts[0]);
//        }
//
//        System.out.println("Checking path...");
//        try {
//            PathValidator.validatePath(methodLineParts[1]);
//        } catch (IllegalArgumentException e) {
//            System.out.println("Malformed HTTP path");
//            throw new HttpRequestParsingException("Malformed HTTP path: " + methodLineParts[1]);
//        }
//        System.out.println("Path check passed");
//        String[] pathParts = methodLineParts[1].split("\\?");
//        if (pathParts.length == 2) {
//            String[] queryParameters = pathParts[1].split("&");
//            for(int i = 0; i < queryParameters.length; i++) {
//                String[] paramParts = queryParameters[i].split("=");
//                httpRequest.queryParameters.put(paramParts[0], paramParts[1]);
//            }
//        }
//        httpRequest.setPath(pathParts[0]);
//
//        httpRequest.httpVersion = methodLineParts[2];
//
//        for (int i = 1; i < headerLines.size(); i++) {
//            String headerLine = headerLines.get(i);
//            String[] headerLineParts = headerLine.split(":", 2);
//            String headerName = headerLineParts[0].trim();
//            String headerValue = headerLineParts[1].trim();
//            httpRequest.headers.put(headerName, headerValue);
//        }
//
//        int contentLength = 0;
//        if (httpRequest.headers.containsKey("Content-Length")) {
//            try {
//                contentLength = Integer.parseInt(httpRequest.headers.get("Content-Length"));
//                if (contentLength < 0) {
//                    throw new HttpRequestParsingException("Invalid Content-Length: " + contentLength);
//                }
//            } catch (NumberFormatException e) {
//                throw new HttpRequestParsingException("Invalid Content-Length format: " + httpRequest.headers.get("Content-Length"));
//            }
//        }
//        return contentLength;
//    }
//
//    private void readBody(HttpRequest httpRequest, int contentLength) throws IOException {
//        if (contentLength > 0) {
//            char[] bodyChars = new char[contentLength];
//            int bytesRead = 0;
//            while (bytesRead < contentLength) {
//                int read = reader.read(bodyChars, bytesRead, contentLength - bytesRead);
//                if (read == -1) {
//                    throw new IOException("Unexpected end of stream while reading body");
//                }
//                bytesRead += read;
//            }
//            httpRequest.body = new String(bodyChars).getBytes();
//            System.out.println("Read: " + Arrays.toString(httpRequest.body));
//        }
//    }
//}
package com.webserver;

import com.webserver.utils.PathValidator;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpReader implements Closeable {
    private BufferedInputStream inputStream;

    public HttpReader(Socket clientSocket) throws IOException {
        if (clientSocket == null) {
            throw new IllegalArgumentException("Client socket cannot be null");
        }
        inputStream = new BufferedInputStream(clientSocket.getInputStream());
    }

    public HttpRequest readRequest() throws IOException, HttpRequestParsingException {
        HttpRequest httpRequest = new HttpRequest();
        System.out.println("Reading headers...");
        int contentLength = readHeaders(httpRequest);
        System.out.println("Headers read. Content-Length: " + contentLength);
        System.out.println("Reading body...");
        readBody(httpRequest, contentLength);
        System.out.println("Body read!");
        return httpRequest;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    private int readHeaders(HttpRequest httpRequest) throws IOException, HttpRequestParsingException {
        List<String> headerLines = new ArrayList<>();
        while (true) {
            String line = readLine();
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

        String methodLine = headerLines.get(0);
        String[] methodLineParts = methodLine.split(" ");
        if (methodLineParts.length != 3)
            throw new HttpRequestParsingException("Request method line does not contain 3 tokens: " + methodLine);


        try {
            httpRequest.method = HttpMethod.valueOf(methodLineParts[0]);
        } catch (IllegalArgumentException e) {
            throw new HttpRequestParsingException("Invalid HTTP method: " + methodLineParts[0]);
        }

        System.out.println("Checking path...");
        try {
            PathValidator.validatePath(methodLineParts[1]);
        } catch (IllegalArgumentException e) {
            System.out.println("Malformed HTTP path");
            throw new HttpRequestParsingException("Malformed HTTP path: " + methodLineParts[1]);
        }
        System.out.println("Path check passed");
        String[] pathParts = methodLineParts[1].split("\\?");
        if (pathParts.length == 2) {
            String[] queryParameters = pathParts[1].split("&");
            for(int i = 0; i < queryParameters.length; i++) {
                String[] paramParts = queryParameters[i].split("=");
                httpRequest.queryParameters.put(paramParts[0], paramParts[1]);
            }
        }
        httpRequest.setPath(pathParts[0]);

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

    private String readLine() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = inputStream.read()) != -1) {
            if (b == '\r') {
                int next = inputStream.read();
                if (next == -1) {
                    // end of stream, return what we have
                    return baos.toString("UTF-8");
                } else if (next == '\n') {
                    return baos.toString("UTF-8");
                } else {
                    // We got \r and then something else, so we write \r and then process the next byte
                    baos.write(b);
                    baos.write(next);
                }
            } else if (b == '\n') {
                // We treat \n as a line terminator by itself
                return baos.toString("UTF-8");
            } else {
                baos.write(b);
            }
        }
        return null;
    }

    private void readBody(HttpRequest httpRequest, int contentLength) throws IOException {
        if (contentLength > 0) {
            byte[] bodyBytes = new byte[contentLength];
            int bytesRead = 0;
            while (bytesRead < contentLength) {
                int read = inputStream.read(bodyBytes, bytesRead, contentLength - bytesRead);
                if (read == -1) {
                    throw new IOException("Unexpected end of stream while reading body");
                }
                bytesRead += read;
            }
            httpRequest.body = bodyBytes;
            System.out.println("Read: " + Arrays.toString(httpRequest.body));
        }
    }
}