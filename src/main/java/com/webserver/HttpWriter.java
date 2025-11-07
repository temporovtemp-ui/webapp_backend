package com.webserver;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class HttpWriter implements Closeable {
    OutputStream outputStream;
    BufferedWriter writer;

    public HttpWriter(Socket clientSocket) throws IOException {
        outputStream = clientSocket.getOutputStream();
        writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public void writeResponse(HttpResponse httpResponse) throws IOException, HttpResponseSerializationException {
        if (httpResponse.httpVersion == null)
            throw new HttpResponseSerializationException("HTTP version shall not be null");
        if (httpResponse.httpResponseStatus == null)
            throw new HttpResponseSerializationException("HTTP status code shall not be null");


        String line = httpResponse.httpVersion + " " + httpResponse.httpResponseStatus + "\r\n";
        writer.write(line);
        System.out.println("Line written: '" + line + "'");

        for(String headerName: httpResponse.headers.keySet()) {
            String headerValue = httpResponse.headers.get(headerName);
            if (headerName == null || headerValue == null)
                throw new HttpResponseSerializationException("HTTP header name and header value shall not be null");
            line = headerName + ": " + headerValue + "\r\n";
            writer.write(line);
            System.out.println("Line written: '" + line + "'");
        }

        line = "\r\n";
        writer.write(line);
        System.out.println("Line written: '" + line + "'");

        if (httpResponse.body != null) {
            outputStream.write(httpResponse.body);
            System.out.println("Body bytes sent: " + Arrays.toString(httpResponse.body));
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
        outputStream.close();
    }
}
