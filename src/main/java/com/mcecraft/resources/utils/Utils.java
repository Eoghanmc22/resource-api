package com.mcecraft.resources.utils;

import com.google.gson.*;
import com.mcecraft.resources.persistence.PersistenceStore;
import com.mcecraft.resources.gson.NamespacedIDTypeAdapter;
import com.mcecraft.resources.gson.PersistenceStoreTypeAdapter;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

public class Utils {

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(NamespaceID.class, new NamespacedIDTypeAdapter())
            .registerTypeAdapter(PersistenceStore.class, new PersistenceStoreTypeAdapter())
            .create();

    public static final NamespaceID INTERNAL = NamespaceID.from("resource_api:internal");

    //https://www.geeksforgeeks.org/sha-1-hash-in-java/
    public static @NotNull String hash(byte @NotNull [] resourcePack) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(resourcePack);

            return hashDigestToString(messageDigest);
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull String hashDigestToString(byte @NotNull [] messageDigest) {
        BigInteger no = new BigInteger(1, messageDigest);

        StringBuilder hash = new StringBuilder(no.toString(16));

        while (hash.length() < 40) {
            hash.insert(0, "0");
        }

        return hash.toString();
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

    public static @NotNull <T> JsonElement toJsonTree(@NotNull T obj) {
        return GSON.toJsonTree(obj);
    }

    public static @NotNull JsonElement fromJsonReader(@NotNull Reader reader) {
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
        return mergeJson(a, b, true);
    }

    /**
     * Merges the 2 json elements
     * In the case of a merge conflict, keep the value in from a unless it is null
     *
     * @param a the primary json
     * @param b the json to merge into a
     * @param handleDisplay if display json should be handled as a special case
     * @return the result of the merge
     */
    public static @NotNull JsonElement mergeJson(@NotNull JsonElement a, @NotNull JsonElement b, boolean handleDisplay) {
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

                if ("display".equals(key) && handleDisplay) {
                    val = mergeEachDisplay(val, other);
                } else if (other != null) {
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

    private static @NotNull JsonElement mergeEachDisplay(@NotNull JsonElement a, @NotNull JsonElement b) {
        if (!(a instanceof JsonObject objA) || !(b instanceof JsonObject objB)) {
            return mergeJson(a, b);
        }

        for (Map.Entry<String, JsonElement> entry : objB.entrySet()) {
            String key = entry.getKey();
            JsonElement val = entry.getValue();
            JsonElement other = objA.get(key);

            if (val.equals(other)) {
                continue;
            }

            if (other != null) {
                val = mergeDisplay(val, other);
            }

            objA.add(key, val);
        }

        return objA;
    }

    private static @NotNull JsonElement mergeDisplay(@NotNull JsonElement a, @NotNull JsonElement b) {
        if (!(a instanceof JsonObject objA) || !(b instanceof JsonObject objB)) {
            return mergeJson(a, b);
        }

        for (Map.Entry<String, JsonElement> entry : objB.entrySet()) {
            String key = entry.getKey();
            JsonElement val = entry.getValue();
            JsonElement other = objA.get(key);

            if (val.equals(other)) {
                continue;
            }

            if (other != null) {
                if ((val instanceof JsonArray arrA) && (other instanceof JsonArray arrB) && arrA.size() == arrB.size() && arrA.size() == 3) {
                    switch (key) {
                        case "rotation", "translation" -> {
                            JsonArray newVal = new JsonArray();

                            newVal.add(new JsonPrimitive(arrA.get(0).getAsFloat() + arrB.get(0).getAsFloat()));
                            newVal.add(new JsonPrimitive(arrA.get(1).getAsFloat() + arrB.get(1).getAsFloat()));
                            newVal.add(new JsonPrimitive(arrA.get(2).getAsFloat() + arrB.get(2).getAsFloat()));

                            val = newVal;
                        }
                        case "scale" -> {
                            JsonArray newVal = new JsonArray();

                            newVal.add(new JsonPrimitive(arrA.get(0).getAsFloat() * arrB.get(0).getAsFloat()));
                            newVal.add(new JsonPrimitive(arrA.get(1).getAsFloat() * arrB.get(1).getAsFloat()));
                            newVal.add(new JsonPrimitive(arrA.get(2).getAsFloat() * arrB.get(2).getAsFloat()));

                            val = newVal;
                        }
                        default -> val = mergeDisplay(val, other);
                    }
                } else {
                    val = mergeDisplay(val, other);
                }
            }

            objA.add(key, val);
        }

        return objA;
    }
}
