package com.mcecraft.resources.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Data {

    static @NotNull Data of(@NotNull Path path) {
        return lazy(() -> {
            try (InputStream is = Files.newInputStream(path)) {
                return is.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    static @NotNull Data ofResource(@NotNull String path) {
        return lazy(() -> {
            try (InputStream is = Utils.resourceInputStream(path)) {
                return is.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    static @NotNull Data of(@NotNull String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        return () -> bytes;
    }

    static @NotNull Data of(byte @NotNull [] data) {
        return () -> data;
    }

    // not thread safe
    static @NotNull Data lazy(@NotNull Data data) {
        Once<byte[]> cache = new Once<>(data::bytes);
        return cache::get;
    }

    byte @NotNull [] bytes();
}
