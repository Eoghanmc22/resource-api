package com.mcecraft.resources;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ResourcePack {
    private final Map<Loc, Data> resourcePackIncludedFiles = new ConcurrentHashMap<>();
    private volatile byte [] bytes = null;
    private volatile String hash = null;

    public void include(@NotNull Loc path, @NotNull Data data) {
        if (resourcePackIncludedFiles.put(path, data) != null) {
            throw new RuntimeException("Duplicate file in resource pack!");
        }
    }

    byte @NotNull [] generate() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            zipOutputStream.setLevel(9);

            for (Map.Entry<Loc, Data> entry : resourcePackIncludedFiles.entrySet()) {
                Loc path = entry.getKey();
                Data data = entry.getValue();

                ZipEntry zipEntry = new ZipEntry(path.getPath());

                //remove time metadata
                zipEntry.setTime(-1);

                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(data.bytes());
            }

            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.bytes = byteArrayOutputStream.toByteArray();
    }

    public byte @NotNull [] getBytes() {
        return Objects.requireNonNullElseGet(bytes, this::generate);
    }

    private @NotNull String genHash() {
        return this.hash = Utils.hash(getBytes());
    }

    public synchronized @NotNull String getHash() {
        return Objects.requireNonNullElseGet(hash, this::genHash);
    }

    public void reset() {
        resourcePackIncludedFiles.clear();
        bytes = null;
        hash = null;
    }
}
