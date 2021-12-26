package com.mcecraft.resources.types.block.real;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public record BlockModelMeta(@NotNull NamespaceID model, int xRot, int yRot, boolean uvLock, int weight) {
    public @NotNull BlockModelMeta withRot(int xRot, int yRot) {
        return new BlockModelMeta(model, xRot, yRot, uvLock, weight);
    }

    public @NotNull BlockModelMeta withUVLock(boolean uvLock) {
        return new BlockModelMeta(model, xRot, yRot, uvLock, weight);
    }

    public @NotNull BlockModelMeta withWeight(int weight) {
        return new BlockModelMeta(model, xRot, yRot, uvLock, weight);
    }

    public static @NotNull BlockModelMeta from(@NotNull NamespaceID model) {
        return new BlockModelMeta(model, 0, 0, false, 1);
    }
}
