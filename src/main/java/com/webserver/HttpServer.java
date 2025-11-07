package com.webserver;

import com.webserver.handlers.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private int port;
    private Handler entryHandler;

    public HttpServer(int port, Handler entryHandler) {
        validatePort(port);
        validateEntryHandler(entryHandler);
        this.port = port;
        this.entryHandler = entryHandler;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        validatePort(port);
        this.port = port;
    }

    public Handler getEntryHandler() {
        return entryHandler;
    }

    public void setEntryHandler(Handler entryHandler) {
        validateEntryHandler(entryHandler);
        this.entryHandler = entryHandler;
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
                        System.out.println("Handling request...");
                        entryHandler.handle(request, response);
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

    private void validatePort(int port) {
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("port must be in boundaries [0, 65535]");
    }

    private void validateEntryHandler(Handler entryHandler) {
        if (entryHandler == null)
            throw new IllegalArgumentException("entryHandler must not be null");
    }
}
