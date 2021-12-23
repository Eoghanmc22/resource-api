package com.mcecraft.resources.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minestom.server.utils.NamespaceID;

import java.io.IOException;

public class NamespacedIDTypeAdapter extends TypeAdapter<NamespaceID> {
    @Override
    public void write(JsonWriter out, NamespaceID value) throws IOException {
        out.value(value.asString());
    }

    @Override
    public NamespaceID read(JsonReader in) throws IOException {
        return NamespaceID.from(in.nextString());
    }
}
