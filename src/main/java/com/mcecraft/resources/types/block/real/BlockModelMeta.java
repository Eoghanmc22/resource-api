package com.mcecraft.resources.types.block.real;

import net.minestom.server.utils.NamespaceID;

public record BlockModelMeta(NamespaceID model, int xRot, int yRot, boolean uvLock, int weight) {
    public BlockModelMeta withRot(int xRot, int yRot) {
        return new BlockModelMeta(model, xRot, yRot, uvLock, weight);
    }

    public BlockModelMeta withUVLock(boolean uvLock) {
        return new BlockModelMeta(model, xRot, yRot, uvLock, weight);
    }

    public BlockModelMeta withWeight(int weight) {
        return new BlockModelMeta(model, xRot, yRot, uvLock, weight);
    }

    public static BlockModelMeta from(NamespaceID model) {
        return new BlockModelMeta(model, 0, 0, false, 1);
    }
}
