package com.mcecraft.resources;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class ResourceType<T extends Resource> {
    private final @NotNull Function<NamespaceID, ResourceBuilder<T>> builderSupplier;
    protected final Map<NamespaceID, T> registeredResources = new ConcurrentHashMap<>();

    public ResourceType(@NotNull Function<NamespaceID, ResourceBuilder<T>> builderSupplier) {
        this.builderSupplier = builderSupplier;
    }

    protected @NotNull Function<NamespaceID, ResourceBuilder<T>> getBuilderSupplier() {
        return builderSupplier;
    }

    protected void register(@NotNull ResourceBuilder<T> builder, @NotNull T resource) {
        registeredResources.put(builder.getNamespaceID(), resource);
    }

    protected abstract void generateResourcePack(@NotNull GeneratedResourcePack resourcePack);

    public @Nullable T lookup(@NotNull NamespaceID namespaceID) {
        return registeredResources.get(namespaceID);
    }
}
