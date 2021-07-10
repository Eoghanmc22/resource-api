package com.mcecraft.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GeneratedResourcePack {
    private static final Gson GSON = new GsonBuilder().create();

    private final Map<String, String> resourcePackFiles = new ConcurrentHashMap<>();
    private volatile byte @Nullable [] bytes = null;
    private volatile @Nullable String hash = null;

    public void include(@NotNull String path, @NotNull String text) {
        resourcePackFiles.put(path, text);
    }

    public void include(@NotNull String path, @NotNull List<@NotNull String> text) {
        StringBuilder sb = new StringBuilder();
        text.forEach(s -> sb.append(s).append('\n'));
        include(path, sb.toString());
    }

    public void include(@NotNull String path, @NotNull String @NotNull ... text) {
        include(path, Arrays.asList(text));
    }

    public void include(@NotNull String path, @NotNull StoreWithGson obj) {
        resourcePackFiles.put(path, GSON.toJson(obj));
    }

    public void include(@NotNull String path, @NotNull JsonElement obj) {
        resourcePackFiles.put(path, GSON.toJson(obj));
    }

    private byte @Nullable [] generate() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            zipOutputStream.setLevel(9);

            for (Map.Entry<String, String> stringStringEntry : resourcePackFiles.entrySet()) {
                String path = stringStringEntry.getKey();
                String contents = stringStringEntry.getValue();

                zipOutputStream.putNextEntry(new ZipEntry(path));
                zipOutputStream.write(contents.getBytes(StandardCharsets.UTF_8));
            }
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        resourcePackFiles.clear();
        bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    public synchronized byte @Nullable [] getBytes() {
        if (bytes == null) {
            return generate();
        } else {
            return bytes;
        }
    }

    private @NotNull String genHash() {
        String hash = Utils.hash(getBytes());
        this.hash = hash;
        return hash;
    }

    public synchronized @NotNull String getHash() {
        String hash = this.hash;
        if (hash == null) {
            return genHash();
        } else {
            return hash;
        }
    }

    public void reset() {
        resourcePackFiles.clear();
        bytes = null;
        hash = null;
    }
}
