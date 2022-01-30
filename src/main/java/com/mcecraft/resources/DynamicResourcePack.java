package com.mcecraft.resources;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DynamicResourcePack {

    // a TreeMap is needed to make sure that files are added in a consistent order
    private final Map<Loc, Data> resourcePackIncludedFiles = new TreeMap<>();
    private volatile byte @UnknownNullability [] bytes = null;
    private volatile @UnknownNullability String hash = null;

    public void include(@NotNull Loc path, @NotNull Data data) {
        Data old;
        if ((old = resourcePackIncludedFiles.put(path, data)) != null) {
            if (Arrays.hashCode(data.bytes()) != Arrays.hashCode(old.bytes())) {
                throw new RuntimeException("Multiple, non-matching files with name of `" + path.getPath() + "` in resource pack!");
            }
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
