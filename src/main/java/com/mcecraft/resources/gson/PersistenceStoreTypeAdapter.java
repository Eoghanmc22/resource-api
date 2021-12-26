package com.mcecraft.resources.gson;

import com.google.gson.*;
import com.mcecraft.resources.persistence.PersistenceStore;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

//TODO this needs to be tested
public class PersistenceStoreTypeAdapter implements JsonSerializer<PersistenceStore>, JsonDeserializer<PersistenceStore> {

    @Override
    public @NotNull PersistenceStore deserialize(@NotNull JsonElement json, @NotNull Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        String clazzName = obj.get("clazz").getAsString();
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Class with name " + clazzName + " could not be found, perhaps different extensions are loaded", e);
        }

        JsonElement data = obj.get("data");

        return context.deserialize(data, clazz);
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull PersistenceStore src, @NotNull Type typeOfSrc, @NotNull JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.add("class", new JsonPrimitive(typeOfSrc.getTypeName()));
        obj.add("data", context.serialize(src, typeOfSrc));

        return obj;
    }
}
