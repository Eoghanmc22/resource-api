package com.mcecraft.resources.utils;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public record Include(Loc loc, Data data) {

    public static Include tex(@NotNull String nameSpace, @NotNull String path) {
        return new Include(Loc.of(NamespaceID.from(nameSpace), Loc.TEXTURES), Data.path(path));
    }

    public static Include model(@NotNull String nameSpace, @NotNull String path) {
        return new Include(Loc.of(NamespaceID.from(nameSpace), Loc.MODELS), Data.path(path));
    }
}
