package com.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while(true) {
                try (Socket clientSocket = serverSocket.accept();
                     HttpReader httpReader = new HttpReader(clientSocket);
                     HttpWriter httpWriter = new HttpWriter(clientSocket)) {
                    System.out.println("New client: " + clientSocket.getInetAddress());
                    HttpRequest request;
                    HttpResponse response = new HttpResponse();
                    try {
                        request = httpReader.readRequest();
                        System.out.println("Got request: " + request);
                        response = new HttpResponse();
                        response.httpVersion = "HTTP/1.1";
                        response.httpResponseStatus = HttpResponseStatus.OK_200;
                        response.headers.put("Content-Type", "text/plain");
                        response.headers.put("Connection", "close");
                        response.body = "Bababa bebebe".getBytes();
                        response.headers.put("Content-Length", String.valueOf(response.body.length));
                        System.out.println("Response prepared: " + response);
                    } catch (IOException e) {
                        response = new HttpResponse();
                        response.httpVersion = "HTTP/1.1";
                        response.httpResponseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR_500;
                        response.headers.put("Content-Length", "0");
                        System.out.println("IO error while handling client: " + e);
                    } catch (HttpRequestParsingException e) {
                        response = new HttpResponse();
                        response.httpVersion = "HTTP/1.1";
                        response.httpResponseStatus = HttpResponseStatus.BAD_REQUEST_400;
                        response.headers.put("Content-Length", "0");
                        System.out.println("HTTP parsing error while handling client: " + e);
                    } finally {
                        try {
                            System.out.println("Sending response...");
                            httpWriter.writeResponse(response);
                            System.out.println("Response sent!");
                        } catch (IOException e) {
                            System.out.println("IO error while sending response: " + e);
                        } catch (HttpResponseSerializationException e) {
                            System.out.println("HTTP response serialization error while sending response: " + e);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("IO error while opening resources for handling client: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("IO error while creating server socket: " + e);
        }
    }
}
