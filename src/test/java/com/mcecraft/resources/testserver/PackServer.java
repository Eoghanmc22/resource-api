package com.mcecraft.resources.testserver;

import com.mcecraft.resources.DynamicResourcePack;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class PackServer {

    public static void run(DynamicResourcePack rp) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("0.0.0.0", 8081), 0);

        byte[] resourcePack = rp.getBytes();
        httpServer.createContext("/pack.zip", exchange1 -> {
            try (HttpExchange exchange = exchange1) {
                if ("GET".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(200, resourcePack.length);

                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(resourcePack, 0, resourcePack.length);
                    responseBody.flush();
                }
            }
        });

        httpServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> httpServer.stop(5)));
    }
}
