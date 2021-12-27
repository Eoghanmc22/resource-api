package com.mcecraft.resources;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public abstract class ResourceBuilder<R extends Resource> {

    private final ResourceType<R, ?, ?> resourceType;
    private final ResourceGenerator api;
    private final NamespaceID namespaceID;

    protected ResourceBuilder(@NotNull ResourceGenerator api, @NotNull ResourceType<R, ?, ?> resourceType, @NotNull NamespaceID namespaceID) {
        this.api = api;
        this.namespaceID = namespaceID;
        this.resourceType = resourceType;
    }

    public final @NotNull R build() {
        return build(true);
    }

    public final @NotNull R build(boolean register) {
        R resource = buildImpl();
        if (register) {
            api.register(getResourceType(), getNamespaceID(), resource);
        }
        return resource;
    }

    protected abstract @NotNull R buildImpl();

    protected @NotNull ResourceGenerator getResourceApi() {
        return api;
    }

    public @NotNull ResourceType<R, ?, ?> getResourceType() {
        return resourceType;
    }

    public @NotNull NamespaceID getNamespaceID() {
        return namespaceID;
    }
}
