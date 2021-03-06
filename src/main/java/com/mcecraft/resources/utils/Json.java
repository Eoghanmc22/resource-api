package com.mcecraft.resources.utils;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Json extends Data {
    static @NotNull Json data(@NotNull Data data) {
        if (data instanceof Json json) {
            return json;
        }

        return lazy(new Json() {
            @Override
            public @NotNull JsonElement json() {
                try (Reader r = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes())))) {
                    return Utils.fromJsonReader(r);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public byte @NotNull [] bytes() {
                return data.bytes();
            }
        });
    }

    static @NotNull Json path(@NotNull Path path) {
        return lazy(new Json() {
            final Once<String> data = new Once<>(() -> {
                try {
                    return Files.readString(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            @Override
            public @NotNull JsonElement json() {
                return Json.str(data.get()).json();
            }

            @Override
            public byte @NotNull [] bytes() {
                return data.get().getBytes(StandardCharsets.UTF_8);
            }
        });
    }

    static @NotNull Json str(@NotNull String data) {
        return lazy(new Json() {
            @Override
            public @NotNull JsonElement json() {
                return Utils.GSON.fromJson(data, JsonElement.class);
            }

            @Override
            public byte @NotNull [] bytes() {
                return data.getBytes(StandardCharsets.UTF_8);
            }
        });
    }

    static @NotNull Json json(@NotNull JsonElement jsonElement) {
        return lazy(() -> jsonElement);
    }

    static <T> @NotNull Json gson(@NotNull T obj) {
        return lazy(() -> Utils.toJsonTree(obj));
    }

    static @NotNull Json lazy(@NotNull Json json) {
        Once<JsonElement> jsonCache = new Once<>(json::json);
        Once<byte[]> bytesCache = new Once<>(json::bytes);

        return new Json() {
            @Override
            public @NotNull JsonElement json() {
                return jsonCache.get();
            }

            @Override
            public byte @NotNull [] bytes() {
                return bytesCache.get();
            }
        };
    }

    @NotNull JsonElement json();

    @Override
    default byte @NotNull [] bytes() {
        return Utils.GSON.toJson(json()).getBytes(StandardCharsets.UTF_8);
    }
}
