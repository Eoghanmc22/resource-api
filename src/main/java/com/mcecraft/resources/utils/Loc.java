package com.mcecraft.resources.utils;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class Loc implements Comparable<Loc> {
    public static final String BLOCK_STATES = "blockstates";
    public static final String MODELS = "models";
    public static final String TEXTURES = "textures";

    public static final String BLOCKS = "block/";
    public static final String ITEMS = "item/";

    public static final String UNKNOWN = "unknown";
    public static final NamespaceID UNKNOWN_ID = NamespaceID.from(UNKNOWN, UNKNOWN);

    private final NamespaceID namespace;
    private final String type;
    private final String path;

    private Loc(@NotNull NamespaceID namespace, @NotNull String type) {
        this.namespace = namespace;
        this.type = type;

        String extension = ".json";

        //todo sounds, fonts, other textures
        if (type.equals(TEXTURES)) {
            extension = ".png";
        }

        this.path = "assets/" + namespace.namespace() + "/" + type + "/" + namespace.path() + extension;
    }

    private Loc(@NotNull String path) {
        this.namespace = UNKNOWN_ID;
        this.type = UNKNOWN;
        this.path = path;
    }

    public @NotNull NamespaceID getNamespace() {
        return namespace;
    }

    public @NotNull String getType() {
        return type;
    }

    public @NotNull String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Loc other = (Loc) o;

        return this.path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public int compareTo(@NotNull Loc o) {
        return this.path.compareTo(o.path);
    }

    public static @NotNull Loc any(@NotNull String path) {
        return new Loc(path);
    }

    public static @NotNull Loc of(@NotNull NamespaceID namespace, @NotNull String type) {
        return new Loc(namespace, type);
    }

    public static @NotNull Loc prefix(@NotNull NamespaceID namespace, @NotNull String type, @NotNull String prefix) {
        return new Loc(Utils.prefixPath(namespace, prefix), type);
    }
}
