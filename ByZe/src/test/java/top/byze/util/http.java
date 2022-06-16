package top.byze.util;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;


public class http {
    private static final HttpHandler handler = exchange -> {
        exchange.getResponseHeaders().add("Content-Type:", "text/plain;charset=utf-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write("Hello, world!".getBytes(StandardCharsets.UTF_8));
        os.close();
    };

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8899), 10);
        server.createContext("/Test", handler);
        server.start();
        System.out.println("server started");
    }
}
