package com.mcecraft.resources;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public abstract class ResourceBuilder<R extends Resource> {

    private final ResourceType<R, ?, ?> resourceType;
    private final NamespaceID namespaceID;

    protected ResourceBuilder(@NotNull ResourceType<R, ?, ?> resourceType, @NotNull NamespaceID namespaceID) {
        this.namespaceID = namespaceID;
        this.resourceType = resourceType;
    }

    public final @NotNull R build() {
        return build(true);
    }

    public final @NotNull R build(boolean register) {
        R resource = buildImpl();
        if (register) {
            ResourceApi.register(getResourceType(), getNamespaceID(), resource);
        }
        return resource;
    }

    protected abstract @NotNull R buildImpl();

    public @NotNull ResourceType<R, ?, ?> getResourceType() {
        return resourceType;
    }

    public @NotNull NamespaceID getNamespaceID() {
        return namespaceID;
    }
}
