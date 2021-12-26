package com.mcecraft.resources.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NamespacedIDTypeAdapter extends TypeAdapter<NamespaceID> {
    @Override
    public void write(@NotNull JsonWriter out, @NotNull NamespaceID value) throws IOException {
        out.value(value.asString());
    }

    @Override
    public @NotNull NamespaceID read(@NotNull JsonReader in) throws IOException {
        return NamespaceID.from(in.nextString());
    }
}
