package com.mcecraft.resources;

import com.google.gson.JsonElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface JsonProvider {
    static JsonProvider of(Path path) {
        try {
            JsonElement jsonElement = Utils.GSON.fromJson(Files.newBufferedReader(path), JsonElement.class);

            return () -> jsonElement;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static JsonProvider of(String data) {
        JsonElement jsonElement = Utils.GSON.fromJson(data, JsonElement.class);

        return () -> jsonElement;
    }

    static JsonProvider of(JsonElement jsonElement) {
        return () -> jsonElement;
    }

    JsonElement get();
}
