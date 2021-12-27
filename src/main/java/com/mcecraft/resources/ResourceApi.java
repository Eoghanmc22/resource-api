package com.mcecraft.resources;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceApi {
    public static final ResourceGenerator GLOBAL = new ResourceGenerator();

    public static <R extends Resource, B extends ResourceBuilder<R>> @NotNull B create(@NotNull ResourceType<R, B, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return GLOBAL.create(resourceType, namespaceID);
    }

    public static <R extends Resource> @Nullable R lookup(@NotNull ResourceType<R, ?, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return GLOBAL.lookup(resourceType, namespaceID);
    }

    public static @NotNull DynamicResourcePack generateResourcePack(@NotNull String packDescription) {
        return GLOBAL.generateResourcePack(packDescription);
    }
}
