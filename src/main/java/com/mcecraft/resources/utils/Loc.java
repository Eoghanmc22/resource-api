package com.mcecraft.resources.utils;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class Loc {
    public static final String BLOCK_STATES = "blockstates";
    public static final String MODELS = "models";
    public static final String TEXTURES = "textures";

    public static final String BLOCKS = "block/";
    public static final String ITEMS = "item/";

    public static final String UNKNOWN = "unknown";
    public static final NamespaceID UNKNOWN_ID = NamespaceID.from(UNKNOWN, UNKNOWN);

    private final @NotNull NamespaceID namespace;
    private final @NotNull String type;
    private final @NotNull String path;

    private Loc(@NotNull NamespaceID namespace, @NotNull String type) {
        this.namespace = namespace;
        this.type = type;

        String extension = ".json";

        //todo sounds, fonts, other textures
        if (type.equals(TEXTURES)) {
            extension = ".png";
        }

        this.path = "assets/" + namespace.getDomain() + "/" + type + "/" + namespace.getPath() + extension;
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

    public static Loc any(@NotNull String path) {
        return new Loc(path);
    }

    public static Loc of(@NotNull NamespaceID namespace, @NotNull String type) {
        return new Loc(namespace, type);
    }

    public static Loc prefix(@NotNull NamespaceID namespace, @NotNull String type, @NotNull String prefix) {
        return new Loc(Utils.prefixPath(namespace, prefix), type);
    }
}
