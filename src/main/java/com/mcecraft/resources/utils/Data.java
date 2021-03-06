package com.mcecraft.resources.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Data {

    static @NotNull Data path(@NotNull String path) {
        return path(Path.of(path));
    }

    static @NotNull Data path(@NotNull Path path) {
        return lazy(() -> {
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    static @NotNull Data resource(@NotNull String path) {
        return lazy(() -> {
            try (InputStream is = Utils.resourceInputStream(path)) {
                return is.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    static @NotNull Data str(@NotNull String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        return () -> bytes;
    }

    static @NotNull Data bytes(byte @NotNull [] data) {
        return () -> data;
    }

    // not thread safe
    static @NotNull Data lazy(@NotNull Data data) {
        Once<byte[]> cache = new Once<>(data::bytes);
        return cache::get;
    }

    byte @NotNull [] bytes();
}
