package com.mcecraft.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.mcecraft.resources.traits.StoreWithGson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GeneratedResourcePack {
    private static final Gson GSON = Utils.GSON;

    private final Set<String> usedPaths = ConcurrentHashMap.newKeySet();
    private final Map<String, byte[]> resourcePackGeneratedFiles = new ConcurrentHashMap<>();
    private final Map<String, File> resourcePackIncludedFiles = new ConcurrentHashMap<>();
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


    public void includeFile(@NotNull String path, @NotNull File file) {
        addPath(path);
        resourcePackIncludedFiles.put(path, file);
    }

    public void includeFile(@NotNull String path, @NotNull String file) {
        includeFile(path, new File(file));
    }


    private byte @NotNull [] generate() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            zipOutputStream.setLevel(9);

            for (Map.Entry<String, byte[]> stringStringEntry : resourcePackGeneratedFiles.entrySet()) {
                String path = stringStringEntry.getKey();
                byte[] contents = stringStringEntry.getValue();

                zipOutputStream.putNextEntry(new ZipEntry(path));
                zipOutputStream.write(contents);
            }

            for (Map.Entry<String, File> stringFileEntry : resourcePackIncludedFiles.entrySet()) {
                String path = stringFileEntry.getKey();
                File file = stringFileEntry.getValue();

                try (final FileInputStream inputStream = new FileInputStream(file)) {
                    zipOutputStream.putNextEntry(new ZipEntry(path));
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
        usedPaths.clear();
        resourcePackGeneratedFiles.clear();
        resourcePackIncludedFiles.clear();
        bytes = null;
        hash = null;
    }
}
