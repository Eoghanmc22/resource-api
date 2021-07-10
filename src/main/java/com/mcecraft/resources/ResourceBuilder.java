package com.mcecraft.resources;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public abstract class ResourceBuilder<T extends Resource> {

    private final @NotNull NamespaceID namespaceID;
    private ResourceType<T> resourceType;

    public ResourceBuilder(@NotNull final NamespaceID namespaceID) {
        this.namespaceID = namespaceID;
    }

    public final @NotNull T build() {
        T resource = buildImpl();
        getResourceType().register(this, resource);
        return resource;
    }

    protected abstract @NotNull T buildImpl();

    public @NotNull NamespaceID getNamespaceID() {
        return namespaceID;
    }

    public ResourceType<T> getResourceType() {
        return resourceType;
    }

    @NotNull ResourceBuilder<T> setResourceType(@NotNull ResourceType<T> resourceType) {
        this.resourceType = resourceType;
        return this;
    }
}
