package com.mcecraft.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.mcecraft.resources.gson.StoreWithGson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GeneratedResourcePack {
    private static final Gson GSON = Utils.GSON;

    private final Set<String> usedPaths = ConcurrentHashMap.newKeySet();
    private final Map<String, byte[]> resourcePackGeneratedFiles = new ConcurrentHashMap<>();
    private final Map<String, Path> resourcePackIncludedFiles = new ConcurrentHashMap<>();
    private volatile byte @Nullable [] bytes = null;
    private volatile @Nullable String hash = null;

    private void addPath(@NotNull String path) {
        if (!usedPaths.add(path)) {
            throw new RuntimeException("Duplicate file in resource pack!");
        }
    }


    public void include(@NotNull String path, byte @NotNull [] data) {
        addPath(path);
        resourcePackGeneratedFiles.put(path, data);
    }

    public void include(@NotNull String path, @NotNull String text) {
       include(path, text.getBytes(StandardCharsets.UTF_8));
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
        include(path, GSON.toJson(obj));
    }

    public void include(@NotNull String path, @NotNull JsonElement obj) {
        include(path, GSON.toJson(obj));
    }


    public void includeFile(@NotNull String path, @NotNull Path file) {
        addPath(path);
        resourcePackIncludedFiles.put(path, file);
    }


    private byte @NotNull [] generate() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            zipOutputStream.setLevel(9);

            for (Map.Entry<String, byte[]> stringStringEntry : resourcePackGeneratedFiles.entrySet()) {
                String path = stringStringEntry.getKey();
                byte[] contents = stringStringEntry.getValue();

                ZipEntry zipEntry = new ZipEntry(path);

                //remove meta data
                zipEntry.setTime(-1);

                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(contents);
            }

            for (Map.Entry<String, Path> stringFileEntry : resourcePackIncludedFiles.entrySet()) {
                String path = stringFileEntry.getKey();
                Path file = stringFileEntry.getValue();

                try (final InputStream inputStream = Files.newInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(path);

                    //remove meta data
                    zipEntry.setTime(-1);

                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write(inputStream.readAllBytes());
                }
            }

            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final byte[] bytes = byteArrayOutputStream.toByteArray();

        this.bytes = bytes;

        return bytes;
    }

    public synchronized byte @NotNull [] getBytes() {
        final byte[] bytes = this.bytes;
        return Objects.requireNonNullElseGet(bytes, this::generate);
    }

    private @NotNull String genHash() {
        String hash = Utils.hash(getBytes());
        this.hash = hash;
        return hash;
    }

    public synchronized @NotNull String getHash() {
        String hash = this.hash;
        return Objects.requireNonNullElseGet(hash, this::genHash);
    }

    public void reset() {
        usedPaths.clear();
        resourcePackGeneratedFiles.clear();
        resourcePackIncludedFiles.clear();
        bytes = null;
        hash = null;
    }
}
