package com.mcecraft.resources;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceApi {
    private static final Set<ResourceType<?>> resourceTypesUsed = ConcurrentHashMap.newKeySet();

    public static <T extends Resource> @NotNull ResourceBuilder<T> create(@NotNull ResourceType<T> resourceType, @NotNull NamespaceID namespaceID) {
        resourceTypesUsed.add(resourceType);
        return resourceType.getBuilderSupplier().apply(namespaceID).setResourceType(resourceType);
    }

    public static <T extends Resource> @Nullable T lookup(@NotNull ResourceType<T> resourceType, @NotNull NamespaceID namespaceID) {
        return resourceType.lookup(namespaceID);
    }

    public static GeneratedResourcePack generateResourcePack() {
        GeneratedResourcePack resourcePack = new GeneratedResourcePack();

        resourceTypesUsed.forEach(resourceType -> resourceType.generateResourcePack(resourcePack));

        return resourcePack;
    }
}
