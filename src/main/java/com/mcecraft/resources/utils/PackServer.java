package com.mcecraft.resources.utils;

import com.mcecraft.resources.DynamicResourcePack;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class PackServer {

    private static final HttpServer httpServer;
    private static final HashMap<String, String> hostedPacks = new HashMap<>();
    private static String pathPrefix = "";
    private static InetSocketAddress addr = null;

    static {
        try {
            httpServer = HttpServer.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void start(@NotNull String address, int port) {
        if (addr != null) {
            throw new RuntimeException("The PackServer has already been started");
        }

        addr = new InetSocketAddress(address, port);

        try {
            httpServer.bind(addr, 0);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> httpServer.stop(5)));
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void hostPack(@NotNull DynamicResourcePack rp) {
        String contextPath = getContextPath(rp);

        httpServer.createContext(contextPath, getHandler(rp));
        hostedPacks.put(rp.getHash(), contextPath);
    }

    private static @NotNull String getContextPath(@NotNull DynamicResourcePack rp) {
        return "/" + pathPrefix + rp.getHash() + ".zip";
    }

    private static @NotNull HttpHandler getHandler(@NotNull DynamicResourcePack rp) {
        byte[] resourcePackBin = rp.getBytes();

        return exchange1 -> {
            try (HttpExchange exchange = exchange1) {
                if ("GET".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(200, resourcePackBin.length);

                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(resourcePackBin, 0, resourcePackBin.length);
                    responseBody.flush();
                }
            }
        };
    }

    public static @NotNull String getPathPrefix() {
        return pathPrefix;
    }

    public static void setPathPrefix(@NotNull String pathPrefix) {
        PackServer.pathPrefix = pathPrefix;
    }

    public static @Nullable String getPath(@NotNull String hash) {
        return hostedPacks.get(hash);
    }
}
