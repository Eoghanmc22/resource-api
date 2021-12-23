package com.mcecraft.resources;

import com.google.gson.*;
import com.mcecraft.resources.gson.NamespacedIDTypeAdapter;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Utils {
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(NamespaceID.class, new NamespacedIDTypeAdapter()).create();
    public static final NamespaceID INTERNAL = NamespaceID.from("resource_api:internal");

    //https://www.geeksforgeeks.org/sha-1-hash-in-java/
    public static @NotNull String hash(byte @NotNull [] resourcePack) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(resourcePack);

            BigInteger no = new BigInteger(1, messageDigest);

            StringBuilder hash = new StringBuilder(no.toString(16));

            while (hash.length() < 40) {
                hash.insert(0, "0");
            }

            return hash.toString();
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String BLOCK_STATES = "blockstates";
    public static final String MODELS = "models";
    public static final String TEXTURES = "textures";

    public static @NotNull String resourcePath(@NotNull NamespaceID namespace, @NotNull String objectType) {
        String extension = ".json";

        if (objectType.equals(TEXTURES)) {
            extension = ".png";
        }

        return "assets/" + namespace.getDomain() + "/" + objectType + "/" + namespace.getPath() + extension;
    }

    public static @NotNull NamespaceID prefixDomain(@NotNull NamespaceID namespace, @NotNull String prefix) {
        if (namespace.getDomain().startsWith(prefix)) {
            return namespace;
        }

        return NamespaceID.from(prefix + namespace.getDomain(), namespace.getPath());
    }

    public static @NotNull NamespaceID prefixPath(@NotNull NamespaceID namespace, @NotNull String prefix) {
        if (namespace.getPath().startsWith(prefix)) {
            return namespace;
        }

        return NamespaceID.from(namespace.getDomain(), prefix + namespace.getPath());
    }

    public static @NotNull <T> JsonElement json(@NotNull T obj) {
        return GSON.toJsonTree(obj);
    }

    public static @NotNull JsonElement json(@NotNull Reader reader) {
        return GSON.fromJson(reader, JsonElement.class);
    }

    public static @NotNull InputStream resourceInputStream(@NotNull String name) {
        InputStream resource = Utils.class.getResourceAsStream(name);

        Objects.requireNonNull(resource, "Resource can not be null");

        return resource;
    }

    public static @NotNull Reader resourceReader(@NotNull String name) {
        return new BufferedReader(new InputStreamReader(resourceInputStream(name)));
    }

    /**
     * Merges the 2 json elements
     * In the case of a merge conflict, keep the value in from a unless it is null
     *
     * @param a the primary json
     * @param b the json to merge into a
     * @return the result of the merge
     */
    public static @NotNull JsonElement mergeJson(@NotNull JsonElement a, @NotNull JsonElement b) {
        if (a.getClass() != b.getClass() || a == JsonNull.INSTANCE) {
            if (a != JsonNull.INSTANCE) {
                return a;
            } else {
                return b;
            }
        }

        if (a instanceof JsonObject o) {
            JsonObject objA = o.deepCopy();
            JsonObject objB = b.getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : objB.entrySet()) {
                String key = entry.getKey();
                JsonElement val = entry.getValue();
                JsonElement other = objA.get(key);

                if (val.equals(other)) {
                    continue;
                }

                if (other != null) {
                    val = mergeJson(val, other);
                }

                objA.add(key, val);
            }

            return objA;
        } else if (a instanceof JsonArray arr) {
            JsonArray arrA = arr.deepCopy();
            JsonArray arrB = b.getAsJsonArray();

            if (arrA.size() == arrB.size()) {
                for (int i = 0; i < arrA.size(); i++) {
                    JsonElement val = arrA.get(i);
                    JsonElement other = arrB.get(i);
                    arrA.set(i, mergeJson(val, other));
                }
            }

            return arrA;
        }

        // JsonPrimitive
        return a;
    }
}
