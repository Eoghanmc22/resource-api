package com.mcecraft.resources.utils;

import com.mcecraft.resources.DynamicResourcePack;
import com.mcecraft.resources.ResourceApi;
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

    private final HttpServer httpServer;
    private final HashMap<String, String> hostedPacks = new HashMap<>();
    private String pathPrefix = "";
    private InetSocketAddress addr = null;

    public PackServer() {
        try {
            httpServer = HttpServer.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        start(ResourceApi.DEFAULT_ADDRESS, ResourceApi.DEFAULT_PORT);
    }

    public void start(@NotNull String address, int port) {
        if (addr != null) {
            throw new RuntimeException("The PackServer has already been started");
        }

        addr = new InetSocketAddress(address, port);

        try {
            httpServer.bind(addr, 0);

            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        httpServer.stop(5);
    }

    public void hostPack(@NotNull DynamicResourcePack rp) {
        String contextPath = getContextPath(rp);

        httpServer.createContext(contextPath, getHandler(rp));
        hostedPacks.put(rp.getHash(), contextPath);
    }

    private @NotNull String getContextPath(@NotNull DynamicResourcePack rp) {
        return "/" + pathPrefix + rp.getHash() + ".zip";
    }

    private @NotNull HttpHandler getHandler(@NotNull DynamicResourcePack rp) {
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

    public @NotNull String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(@NotNull String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public @Nullable String getPath(@NotNull String hash) {
        return hostedPacks.get(hash);
    }
}
